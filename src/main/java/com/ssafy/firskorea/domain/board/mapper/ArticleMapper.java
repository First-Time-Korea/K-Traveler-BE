package com.ssafy.firskorea.domain.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.firskorea.domain.board.dto.ArticleDto;
import com.ssafy.firskorea.domain.board.dto.FileDto;
import com.ssafy.firskorea.domain.board.dto.TagDto;
import com.ssafy.firskorea.domain.board.dto.response.ArticleAndCommentDto;

@Mapper
public interface ArticleMapper {

	Integer getArticleTagId(String tag) throws Exception;

	void writeArticleTag(TagDto tag) throws Exception;

	void writeArticle(ArticleDto article) throws Exception;

	void connectArticleAndTag(Map<String, Object> map) throws Exception;

	void writeArticleFile(FileDto file) throws Exception;

	List<ArticleDto> getArticles(Map<String, Object> map) throws Exception;

	int getTotalArticleCount(Map<String, Object> map) throws Exception;

	ArticleAndCommentDto getArticle(int articleId) throws Exception;

	ArticleDto getArticleForModification(int articleId) throws Exception;

	List<TagDto> getTagsOfArticle(int articleId) throws Exception;

	void modifyArticle(Map<String, Object> map) throws Exception;

	void disconnectArticleAndTag(Map<String, Object> map) throws Exception;

	void deleteArticleFile(int articleId) throws Exception;

	void deleteArticle(int aritlceId) throws Exception;

}
