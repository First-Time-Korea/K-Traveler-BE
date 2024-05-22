package com.ssafy.firskorea.attraction.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.firskorea.attraction.dto.request.SearchDto;
import com.ssafy.firskorea.attraction.dto.response.AttractionDto;
import com.ssafy.firskorea.attraction.dto.response.BookmarkedAttractionInfoDto;
import com.ssafy.firskorea.attraction.dto.response.ThemeDto;

@Mapper
public interface AttractionMapper {

	List<ThemeDto> getThemeList() throws SQLException;

	List<Category> getCategoryList(Character code) throws SQLException;

	List<AttractionDto> getAttractionBySearch(SearchDto searchDto) throws SQLException;

	void createBookmark(Map<String, String> map) throws SQLException;

	AttractionDto getAttractionByContentId(Map<String, String> map) throws SQLException;

	void deleteBookmark(Map<String, String> map) throws SQLException;

	List<AttractionDto> getBookmarkedAttractionList(String memberId) throws SQLException;

	List<AttractionDto> getAttractionListBySidoCode(String sidoCode) throws SQLException;

	List<Category> getSidoList() throws SQLException;
	
	List<BookmarkedAttractionInfoDto> getBookmarkedAttractionInfos(Map<String, Object> map) throws Exception;
	
	int getTotalBookmarkedAttractionCount(String memberId) throws Exception;
}
