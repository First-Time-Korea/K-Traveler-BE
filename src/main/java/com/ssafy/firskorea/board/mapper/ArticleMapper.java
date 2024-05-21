package com.ssafy.firskorea.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.firskorea.board.dto.ArticleDto;
import com.ssafy.firskorea.board.dto.FileDto;
import com.ssafy.firskorea.board.dto.TagDto;
import com.ssafy.firskorea.board.dto.response.ArticleAndCommentDto;

@Mapper
public interface ArticleMapper {

	List<ArticleDto> getArticles(Map<String, Object> map) throws Exception;

	int getTotalArticleCount(Map<String, Object> map) throws Exception;
	
	Integer getArticleTagId(String tag) throws Exception;
	
	void writeArticleTag(TagDto tag) throws Exception;
	
	void writeArticle(ArticleDto article) throws Exception;
	
	void connectArticleAndTag(Map<String, Object> map) throws Exception;
	
	void writeArticleFile(FileDto file) throws Exception;
	
	ArticleDto getArticleForModification(int articleId) throws Exception;
	
	List<TagDto> getTagsOfArticle(int articleId) throws Exception;
	
	void modifyArticle(Map<String, Object> map) throws Exception;
	
	void disconnectArticleAndTag(Map<String, Object> map) throws Exception;
	
	ArticleAndCommentDto getArticle(int articleId) throws Exception;
	
	void deleteArticleFile(int articleId) throws Exception;
	
	void deleteArticle(int aritlceId) throws Exception;

}
