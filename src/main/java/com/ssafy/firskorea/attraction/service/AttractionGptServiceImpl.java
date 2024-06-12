package com.ssafy.firskorea.attraction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.firskorea.attraction.constant.PromptMessage;
import com.ssafy.firskorea.attraction.dto.request.AttractionIdentityDto;
import com.ssafy.firskorea.attraction.dto.response.AttractionDto;
import com.ssafy.firskorea.attraction.mapper.AttractionMapper;
import com.ssafy.firskorea.config.ChatGPTConfig;
import com.ssafy.firskorea.attraction.dto.request.CompletionRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class AttractionGptServiceImpl implements AttractionGptService {

	@Autowired
	private AttractionMapper attractionMapper;
	private final ChatGPTConfig chatGPTConfig;
	private final ObjectMapper objectMapper;

	public AttractionGptServiceImpl(ChatGPTConfig chatGPTConfig) {
		this.chatGPTConfig = chatGPTConfig;
		this.objectMapper = new ObjectMapper(); // jackson 직렬화 라이브러리
	}

	@Value("${openai.model}")
	private String model;

	@Override
	@Transactional
	public AttractionDto prompt(AttractionIdentityDto attractionIdentityDto) throws Exception {
		AttractionDto attractionDto = attractionMapper.getAttractionByContentId(attractionIdentityDto);
		if (attractionDto != null) {
			// 그냥 반환하기
			return attractionDto;
		}

		attractionDto = attractionMapper.getKCurtureAttractionByContentId(attractionIdentityDto);
		CompletionRequestDto completionRequestDto = new CompletionRequestDto("gpt-3.5-turbo",
				List.of(new CompletionRequestDto.Message("system",
						PromptMessage.MESSAGE.formatMessage(attractionDto.getTitle(), attractionDto.getOverView()),
						(float) 0)));

		HttpHeaders headers = chatGPTConfig.httpHeaders();
		String requestBody = serialize(completionRequestDto);

		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<String> response = chatGPTConfig.restTemplate()
				.exchange("https://api.openai.com/v1/chat/completions", HttpMethod.POST, requestEntity, String.class);

		Map<String, Object> parsed1 = deserialize(response.getBody());
		ArrayList list = (ArrayList) parsed1.get("choices");
		LinkedHashMap parsed2 = (LinkedHashMap) list.get(0);
		LinkedHashMap parsed3 = (LinkedHashMap) parsed2.get("message");
		String[] englishItem = String.valueOf(parsed3.get("content")).split("___"); // ___로 구분, 파싱

		String englishOverView = englishItem[1];
		String englishTitle = englishItem[0];
		attractionDto.setTitle(englishTitle + "(" + attractionDto.getTitle() + ")");
		attractionDto.setOverView(englishOverView);

		// 저장하기
		attractionMapper.insertKCurtureAttractionInfoEnglish(attractionDto);
		attractionMapper.insertKCurtureAttractionDetailEnglish(attractionDto);
		attractionMapper.insertKCurtureAttractionDescriptionEnglish(attractionDto);
		return attractionDto;
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