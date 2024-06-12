package com.ssafy.firskorea.attraction.service;
import com.ssafy.firskorea.attraction.dto.request.MemberContentDto;
import com.ssafy.firskorea.attraction.dto.response.AttractionDto;
import org.springframework.stereotype.Service;


@Service
public interface AttractionGptService {

    AttractionDto getAttractionDetailWithAI(MemberContentDto memberContentDto) throws Exception;
}