package com.ssafy.firskorea.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.firskorea.board.dto.ArticleDto;
import com.ssafy.firskorea.board.mapper.ArticleMapper;
import com.ssafy.firskorea.util.SizeConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

	private final ArticleMapper articleMapper;

	public ArticleServiceImpl(ArticleMapper articleMapper) {
		super();
		this.articleMapper = articleMapper;
	}

	@Override
	public Map<String, Object> getArticles(Map<String, String> map) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		// 여행 후기 리스트 가져오기
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("key", map.get("key"));
		param.put("word", map.get("word") == null ? "" : map.get("word"));
		int pgNo = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
		int start = pgNo * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;
		param.put("start", start);
		param.put("listsize", SizeConstant.LIST_SIZE);

		List<ArticleDto> articles = articleMapper.getArticles(param);
		result.put("articles", articles);

		// 페이지네비게이션 계산하기
		int currentPage = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
		int sizePerPage = SizeConstant.LIST_SIZE;
		int totalArticleCount = articleMapper.getTotalArticleCount(param);
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;

		result.put("currentPage", currentPage);
		result.put("totalPageCount", totalPageCount);

		return result;
	}

}
