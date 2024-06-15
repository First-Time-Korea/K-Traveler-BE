package com.ssafy.firskorea.attraction.service;
import com.ssafy.firskorea.attraction.dto.request.MemberContentDto;
import com.ssafy.firskorea.attraction.dto.response.AttractionDto;
import org.springframework.stereotype.Service;

import java.sql.SQLException;


@Service
public interface AttractionGptService {

    AttractionDto getAttractionDetailWithGptApi(MemberContentDto memberContentDto) throws SQLException;
    AttractionDto getAttractionDetailAtDB(MemberContentDto memberContentDto) throws SQLException;
}