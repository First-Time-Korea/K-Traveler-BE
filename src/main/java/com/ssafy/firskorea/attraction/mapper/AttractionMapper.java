package com.ssafy.firskorea.attraction.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale.Category;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.firskorea.attraction.dto.response.ThemeDto;

@Mapper
public interface AttractionMapper {

	List<ThemeDto> getThemeList() throws SQLException;

	List<Category> getCategoryList(Character code) throws SQLException;

}
