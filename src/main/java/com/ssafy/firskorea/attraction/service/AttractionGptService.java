package com.ssafy.firskorea.attraction.service;
import com.ssafy.firskorea.attraction.dto.response.AttractionDto;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Map;



@Service
public interface AttractionGptService {

    AttractionDto prompt(Map<String, String> map) throws SQLException, Exception;
}