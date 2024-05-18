package com.ssafy.firskorea.attraction.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;

import com.ssafy.firskorea.attraction.dto.request.SearchDto;
import com.ssafy.firskorea.attraction.dto.response.AttractionDto;
import com.ssafy.firskorea.attraction.dto.response.ThemeDto;

public interface AttractionService {

	List<ThemeDto> getThemeList() throws SQLException;

	List<Category> getCategoryList(Character code) throws SQLException;

	List<AttractionDto> search(SearchDto searchDto) throws SQLException;

    AttractionDto toggleBookmark(Map<String, String> map) throws SQLException;

}
