package com.ssafy.firskorea.attraction.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;

import com.ssafy.firskorea.attraction.dto.request.MemberContentDto;
import org.apache.ibatis.annotations.Mapper;

import com.ssafy.firskorea.attraction.dto.request.SearchDto;
import com.ssafy.firskorea.attraction.dto.response.AttractionDto;
import com.ssafy.firskorea.attraction.dto.response.ThemeDto;

@Mapper
public interface AttractionMapper {

	List<ThemeDto> getThemeList() throws SQLException;

	List<Category> getCategoryList(Character code) throws SQLException;

	List<AttractionDto> getAttractionBySearch(SearchDto searchDto) throws SQLException;

	void createAttractionBookmark(MemberContentDto memberContentDto) throws SQLException;

	AttractionDto getAttractionByContentId(MemberContentDto memberContentDto) throws SQLException;

	void deleteAttractionBookmark(MemberContentDto memberContentDto) throws SQLException;

	List<AttractionDto> getAllAttractionsBookmarked(String memberId) throws SQLException;

	List<AttractionDto> getPaginatedAttractionsBySidoCode(Map<String, Object> map) throws SQLException;
	
	int getTotalAttractionsBySidoCodeCount(int sidoCode) throws SQLException;

	List<Category> getSidoList() throws SQLException;

	List<AttractionDto> getPaginatedAttractionsBookmarked(Map<String, Object> map) throws SQLException;

	int getTotalAttractionsBookmarkedCount(String memberId) throws SQLException;

	AttractionDto getKCurtureAttractionByContentId(MemberContentDto memberContentDto) throws SQLException;

	void insertKCurtureAttractionInfoEnglish(AttractionDto attractionDto) throws SQLException;

	void insertKCurtureAttractionDetailEnglish(AttractionDto attractionDto) throws SQLException;

	void insertKCurtureAttractionDescriptionEnglish(AttractionDto attractionDto) throws SQLException;
}
