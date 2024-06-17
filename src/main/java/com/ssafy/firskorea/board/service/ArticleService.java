package com.ssafy.firskorea.board.service;

import java.util.Map;

import com.ssafy.firskorea.board.dto.ArticleDto;
import com.ssafy.firskorea.board.dto.request.SearchDto;
import com.ssafy.firskorea.board.dto.response.ArticleAndCommentDto;

public interface ArticleService {

	void writeArticle(Map<String, Object> map) throws Exception;

	Map<String, Object> getArticles(SearchDto search) throws Exception;

	ArticleAndCommentDto getArticle(int articleId) throws Exception;

	ArticleDto getArticleForModification(int articleId) throws Exception;

	void modifyArticle(Map<String, Object> map) throws Exception;

	void deleteArticle(int articleId) throws Exception;

}
