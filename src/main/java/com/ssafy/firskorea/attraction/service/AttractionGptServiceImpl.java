package com.ssafy.firskorea.attraction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.firskorea.attraction.constant.PromptMessage;
import com.ssafy.firskorea.attraction.dto.request.MemberContentDto;
import com.ssafy.firskorea.attraction.dto.response.AttractionDto;
import com.ssafy.firskorea.attraction.mapper.AttractionMapper;
import com.ssafy.firskorea.common.consts.RetConsts;
import com.ssafy.firskorea.common.exception.KTravelerException;
import com.ssafy.firskorea.config.ChatGPTConfig;
import com.ssafy.firskorea.attraction.dto.prompt.CompletionRequestDto;
import com.ssafy.firskorea.util.GptApiValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

@Slf4j
@Service
public class AttractionGptServiceImpl implements AttractionGptService {

    private final AttractionMapper attractionMapper;
    private final ChatGPTConfig chatGPTConfig;
    private final ObjectMapper objectMapper;
    private final int RETRY_CNT = 5;
    private final String SEPARATOR = "___";

    @Value("${openai.model}")
    private String model;

    public AttractionGptServiceImpl(AttractionMapper attractionMapper, ChatGPTConfig chatGPTConfig, ObjectMapper objectMapper) {
        this.attractionMapper = attractionMapper;
        this.chatGPTConfig = chatGPTConfig;
        this.objectMapper = objectMapper;
    }

    //여기서 transactional을 걸면 강제 종료 했을 때 롤백됨
    public AttractionDto getAttractionDetailWithGptApi(MemberContentDto memberContentDto) throws SQLException {
        return processAttraction(memberContentDto);
    }

    public AttractionDto getAttractionDetailAtDB(MemberContentDto memberContentDto) throws SQLException {
        return attractionMapper.getAttractionByContentId(memberContentDto);
    }

    @Transactional
    public AttractionDto processAttraction(MemberContentDto memberContentDto) throws SQLException, KTravelerException {
        AttractionDto attractionDto = attractionMapper.getAttractionByContentId(memberContentDto);
        if (attractionDto == null) {
            attractionDto = fetchAndTranslateAttraction(memberContentDto); //GPT가 잘못 번역하면 예외 발생
            storeAttractionData(attractionDto); //GPT가 문제 없이 번역했으면 DB에 반영.
        }
        return attractionDto;
    }

    private AttractionDto fetchAndTranslateAttraction(MemberContentDto memberContentDto) throws SQLException, KTravelerException {
        AttractionDto attractionDto = attractionMapper.getKCurtureAttractionByContentId(memberContentDto);
        if (attractionDto == null) {
            throw new SQLException(); //해당 content Id가 존재하지 않음
        }

        //K Culture에 있는 값 파싱
        String[] mediaAndDescription = attractionDto.getOverView().split(">");
        String media = mediaAndDescription[0].replace("<", "");
        String description = mediaAndDescription[1];
        String addr = attractionDto.getAddr1();
        String title = attractionDto.getTitle();

        //GPT 호출할 프롬프트 세팅
        CompletionRequestDto completionRequestDto = setGptPrompt(media, description, addr, title);

        //GPT 가 잘 번역할 때까지 3번 기회 줌
        int tryCount = 0;
        while (tryCount < RETRY_CNT) {
            //실제 API 호출
            ResponseEntity<String> response = callGptApi(completionRequestDto);
            AttractionDto translated = handleApiResponse(attractionDto, media, response);
            if (translated != null) {
                return translated;
            }
            tryCount++;
        }
        throw new KTravelerException(RetConsts.ERR604);
    }

    private ResponseEntity<String> callGptApi(CompletionRequestDto completionRequestDto) {
        HttpHeaders headers = chatGPTConfig.httpHeaders();
        String requestBody = serialize(completionRequestDto);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        return chatGPTConfig.restTemplate()
                .exchange("https://api.openai.com/v1/chat/completions", HttpMethod.POST, requestEntity, String.class);
    }

    private CompletionRequestDto setGptPrompt(String media, String description, String addr, String title) {
        return new CompletionRequestDto(model,
                List.of(new CompletionRequestDto.Message("user",
                        PromptMessage.MESSAGE.formatMessage(title, addr, media, description),
                        (float) 0)));
    }

    private AttractionDto handleApiResponse(AttractionDto attractionDto, String media, ResponseEntity<String> response) {
        Map<String, Object> parsed1 = deserialize(response.getBody());
        ArrayList list = (ArrayList) parsed1.get("choices");
        LinkedHashMap parsed2 = (LinkedHashMap) list.get(0);
        LinkedHashMap parsed3 = (LinkedHashMap) parsed2.get("message");
        String[] englishItem = String.valueOf(parsed3.get("content")).split(SEPARATOR); // ___로 구분, 파싱

        if (!GptApiValidator.isValidLength(englishItem)) { //사용해야 할 요소가 총 4개거든
            return null; // 재시도를 유발
        }

        return parseApiResponse(attractionDto, media, englishItem);
    }

    private AttractionDto parseApiResponse(AttractionDto attractionDto, String media, String[] englishItem) {
        String englishTitle = englishItem[0];
        String englishAddr = englishItem[1];
        String englishOverView = "<" + englishItem[2] + "(" + media + ")>" + englishItem[3];

        if (!GptApiValidator.isValidTitle(englishTitle)
                || !GptApiValidator.isValidAddr(englishAddr)
                || !GptApiValidator.isValidOverView(englishOverView)) { //영어로 잘 번역되었는지, 올바른 포맷으로 번역 되었는지 확인
            return null; // 재시도를 유발
        }

        attractionDto.setTitle(englishTitle + "(" + attractionDto.getTitle() + ")");
        attractionDto.setOverView(englishOverView);
        attractionDto.setAddr1(englishAddr);

        return attractionDto;
    }

    private void storeAttractionData(AttractionDto attractionDto) throws SQLException {
        attractionMapper.insertKCurtureAttractionInfoEnglish(attractionDto);
        attractionMapper.insertKCurtureAttractionDetailEnglish(attractionDto);
        attractionMapper.insertKCurtureAttractionDescriptionEnglish(attractionDto);
    }

    private String serialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("직렬화 실패", e);
        }
    }

    private Map<String, Object> deserialize(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("역직렬화 실패", e);
        }
    }
}