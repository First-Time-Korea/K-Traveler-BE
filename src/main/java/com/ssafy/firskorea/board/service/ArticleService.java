package com.ssafy.firskorea.board.service;

import java.util.Map;

public interface ArticleService {
	
	void writeArticle(Map<String, Object> map) throws Exception;
	Map<String, Object> getArticles(Map<String, String> map) throws Exception;
	byte[] getArticleFile(String src) throws Exception;

}
