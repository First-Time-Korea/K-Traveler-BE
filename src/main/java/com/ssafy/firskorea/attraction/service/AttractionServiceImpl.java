package com.ssafy.firskorea.attraction.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.firskorea.attraction.dto.request.SearchDto;
import com.ssafy.firskorea.attraction.dto.response.AttractionDto;
import com.ssafy.firskorea.attraction.dto.response.ThemeDto;
import com.ssafy.firskorea.attraction.mapper.AttractionMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttractionServiceImpl implements AttractionService {

	@Autowired
	private AttractionMapper attractionMapper;

	@Override
	public List<ThemeDto> getThemeList() throws SQLException {
		return attractionMapper.getThemeList();
	}

	@Override
	public List<Category> getCategoryList(Character code) throws SQLException {
		return attractionMapper.getCategoryList(code);
	}

	@Override
	public List<AttractionDto> getAttractionByKeywordAndCode(SearchDto searchDto) throws SQLException {
		return attractionMapper.getAttractionByKeywordAndCode(searchDto);
	}

	@Override
	@Transactional
	public AttractionDto toggleBookmark(Map<String, String> map) throws SQLException {
		AttractionDto dto = attractionMapper.getAttractionByContentId(map);
		if(dto.getBookmarkId()>0){
			attractionMapper.deleteBookmark(map);
		}else{
			attractionMapper.createBookmark(map);
		}
		return attractionMapper.getAttractionByContentId(map);
	}

	@Override
	public AttractionDto getAttractionById(Map<String, String> map) throws SQLException {
		return attractionMapper.getAttractionByContentId(map);
	}
}
