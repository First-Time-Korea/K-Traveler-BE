package com.ssafy.firskorea.attraction.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.firskorea.attraction.dto.response.ThemeDto;
import com.ssafy.firskorea.attraction.mapper.AttractionMapper;

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

}
