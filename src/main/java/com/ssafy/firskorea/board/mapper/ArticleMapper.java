package com.ssafy.firskorea.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.firskorea.board.dto.ArticleDto;
import com.ssafy.firskorea.board.dto.FileDto;
import com.ssafy.firskorea.board.dto.TagDto;

@Mapper
public interface ArticleMapper {

	List<ArticleDto> getArticles(Map<String, Object> map) throws Exception;

	int getTotalArticleCount(Map<String, Object> map) throws Exception;
	
	Integer getArticleTagId(String tag) throws Exception;
	
	void writeArticleTag(TagDto tag) throws Exception;
	
	void writeArticle(ArticleDto article) throws Exception;
	
	void connectArticleAndTag(Map<String, Object> map) throws Exception;
	
	void writeArticleFile(FileDto file) throws Exception;

}
