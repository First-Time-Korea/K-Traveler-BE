package com.ssafy.firskorea.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.firskorea.board.dto.ArticleDto;

@Mapper
public interface ArticleMapper {

	List<ArticleDto> getArticles(Map<String, Object> map) throws Exception;

	int getTotalArticleCount(Map<String, Object> map) throws Exception;

}
