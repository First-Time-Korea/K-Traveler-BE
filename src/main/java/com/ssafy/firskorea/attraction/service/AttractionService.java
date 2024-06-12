package com.ssafy.firskorea.attraction.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;

import com.ssafy.firskorea.attraction.dto.request.AttractionIdentityDto;
import com.ssafy.firskorea.attraction.dto.request.SearchDto;
import com.ssafy.firskorea.attraction.dto.response.AttractionDto;
import com.ssafy.firskorea.attraction.dto.response.ThemeDto;

public interface AttractionService {

    List<ThemeDto> getThemeList() throws SQLException;

    List<Category> getCategoryList(Character code) throws SQLException;

    List<AttractionDto> getAttractionBySearch(SearchDto searchDto) throws SQLException;

    AttractionDto toggleBookmark( AttractionIdentityDto attractionIdentityDto) throws SQLException;

    AttractionDto getAttractionById( AttractionIdentityDto attractionIdentityDto) throws SQLException;

    Map<String, Object> getAttractionsBySidoCode(Map<String, String> map) throws Exception;

    List<AttractionDto> getBookmarkedAttractionList(String memberId) throws SQLException;

    List<Category> getSidoList() throws SQLException;
	
	Map<String, Object> getBookmarkedAttractionInfos(Map<String, String> map) throws Exception;
}
