package com.ssafy.firskorea.attraction.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;

import com.ssafy.firskorea.attraction.dto.request.AttractionIdentityDto;
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

	void createBookmark(AttractionIdentityDto map) throws SQLException;

	AttractionDto getAttractionByContentId(AttractionIdentityDto map) throws SQLException;

	void deleteBookmark(AttractionIdentityDto map) throws SQLException;

	List<AttractionDto> getBookmarkedAttractionList(String memberId) throws SQLException;

	List<AttractionDto> getAttractionsBySidoCode(Map<String, Object> map) throws SQLException;
	
	int getTotalAttractionsBySidoCodeCount(int sidoCode) throws Exception;

	List<Category> getSidoList() throws SQLException;

	List<BookmarkedAttractionInfoDto> getBookmarkedAttractionInfos(Map<String, Object> map) throws Exception;

	int getTotalBookmarkedAttractionCount(String memberId) throws Exception;

	AttractionDto getKCurtureAttractionByContentId(AttractionIdentityDto map) throws Exception;

	void insertKCurtureAttractionInfoEnglish(AttractionDto attractionDto) throws Exception;

	void insertKCurtureAttractionDetailEnglish(AttractionDto attractionDto) throws Exception;

	void insertKCurtureAttractionDescriptionEnglish(AttractionDto attractionDto) throws Exception;
}
