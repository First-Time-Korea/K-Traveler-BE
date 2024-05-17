package com.ssafy.firskorea.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.firskorea.attraction.dto.response.ThemeDto;
import com.ssafy.firskorea.attraction.service.AttractionService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * 검색 및 비동기 처리 전용 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/attraction")
@CrossOrigin(origins = "*")
public class AttractionController {

	private AttractionService attractionService;

	public AttractionController(AttractionService attractionService) {
		super();
		this.attractionService = attractionService;
	}

	@Operation(summary = "여행 테마", description = "여행지 탭에 들어가면, 기본적으로 여행 테마를 불러온다.")
	@GetMapping("/theme")
	public ResponseEntity<Map<String, Object>> getThemeList() throws SQLException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		System.out.println("여행 테마 조회");
		List<ThemeDto> list = attractionService.getThemeList();
		System.out.println(list);
		resultMap.put("status", "success");
		resultMap.put("data", list);
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@Operation(summary = "여행 카테고리", description = "여행 테마에 맞는 여행 카테고리를 조회한다.")
	@GetMapping("/theme/{themaCode}/category")
	public ResponseEntity<Map<String, Object>> getCategoryList(@PathVariable Character themaCode) throws SQLException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		System.out.println("여행 카테고리 조회");
		List<Category> list = attractionService.getCategoryList(themaCode);
		System.out.println(list);
		resultMap.put("status", "success");
		resultMap.put("data", list);
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

}