package com.ssafy.firskorea.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.firskorea.board.service.ArticleService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@CrossOrigin(origins = { "*" }, maxAge = 60000)
@RestController
@RequestMapping("/article")
public class ArticleController {
	
	private final ArticleService articleService;

	public ArticleController(ArticleService articleService) {
		super();
		this.articleService = articleService;
	}
	
	// 여행 후기 리스트 조회하기
	@GetMapping("/list")
	public ResponseEntity<Map<String, Object>> getMethodName(@RequestParam Map<String, String> map) throws Exception {
		Map<String, Object> result = articleService.getArticles(map);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "게시글 목록 조회 성공");
		response.put("articles", result.get("articles"));
		response.put("currentPage", result.get("currentPage"));
		response.put("totalPageCount", result.get("totalPageCount"));

		ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.status(200).body(response);

		return responseEntity;

	}
	

}
