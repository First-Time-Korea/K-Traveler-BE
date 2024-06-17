package com.ssafy.firskorea.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.firskorea.board.dto.ArticleDto;
import com.ssafy.firskorea.board.dto.response.ArticleAndCommentDto;
import com.ssafy.firskorea.board.service.ArticleService;
import com.ssafy.firskorea.common.consts.RetConsts;
import com.ssafy.firskorea.common.dto.CommonResponse;
import com.ssafy.firskorea.util.CommentStratify;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "여행 후기 컨트롤러")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/article")
public class ArticleController {

	private final ArticleService articleService;

	public ArticleController(ArticleService articleService) {
		super();
		this.articleService = articleService;
	}

	@Operation(summary = "여행 후기 작성")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "여행 후기 작성 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
			@ApiResponse(responseCode = "401", description = "유효하지 않은 회원 아이디")
	})
	@PostMapping("/write")
	public CommonResponse<?> writeArticle(@RequestParam("userid") String userId,
			@RequestParam("tags") List<String> tags, @RequestParam(value = "content", required = false) String content,
			@RequestParam("file") MultipartFile file) throws Exception {
		Map<String, Object> map = new HashMap<>();
		
		// 입력값 유효성 검사하기
		if (userId.equals("") || tags.isEmpty() || file.isEmpty()) {
			return CommonResponse.failure(RetConsts.ERR400, "입력값에 대한 유효성 검사를 실패했습니다.");
		}
		
		map.put("userId", userId);
		map.put("tags", tags);
		map.put("file", file);
		
		if (content == null) {
			map.put("content", "");
		} else {
			map.put("content", content);
		}

		try {
			articleService.writeArticle(map);

			return CommonResponse.ok();
		} catch (DataIntegrityViolationException e) {  // 유효하지 않은 회원 아이디인 경우
			return CommonResponse.failure(RetConsts.ERR401, "입력값에 대한 유효성 검사를 실패했습니다.");
		}
	}

	@Operation(summary = "여행 후기 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "여행 후기 리스트 조회 성공"),
	})
	@GetMapping("/list")
	public CommonResponse<?> getArticles(@RequestParam Map<String, String> map) throws Exception {
		Map<String, Object> result = articleService.getArticles(map);

		Map<String, Object> responseData = new HashMap<>();
		responseData.put("articles", result.get("articleFiles"));
		responseData.put("currentPage", result.get("currentPage"));
		responseData.put("totalPageCount", result.get("totalPageCount"));

		return CommonResponse.ok(responseData);
	}
	
	@Operation(summary = "여행 후기 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "여행 후기 조회 성공"),
	})
	@GetMapping("/detail/{articleid}")
	public CommonResponse<?> getArticle(@PathVariable("articleid") int articleId) throws Exception {
		ArticleAndCommentDto ac = articleService.getArticle(articleId);
		
		// 탈퇴한 여행 후기인 경우 작성자 처리
		if (!ac.getExistedOfMember()) {
			ac.setMemberId("(withdrawn member)");
		}
		
		if (ac.getComments() != null) {
			CommentStratify.stratify(ac.getComments());
		}
		
		Map<String, Object> responseData = new HashMap<>();
		responseData.put("article", ac);

		return CommonResponse.ok(responseData);
	}
	
	@Operation(summary = "여행 후기 조회 for 수정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "여행 후기 조회 for 수정 성공"),
	})
	@GetMapping("/modify/{articleid}")
	public CommonResponse<?> getArticleForModification(@PathVariable("articleid") int articleId) throws Exception {
		ArticleDto article = articleService.getArticleForModification(articleId);
		
		Map<String, Object> responseData = new HashMap<>();
		responseData.put("article", article);

		return CommonResponse.ok(responseData);
	}
	
	@Operation(summary = "여행 후기 수정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "여행 후기 수정 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
	})
	@PutMapping("/modify")
	public CommonResponse<?> modifyArticle(@RequestParam("articleid") int articleId,
			@RequestParam("tags") List<String> tags, @RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		Map<String, Object> map = new HashMap<>();
		
		// 입력값 유효성 검사하기
		if (tags.isEmpty() || (file != null && file.isEmpty())) {
			return CommonResponse.failure(RetConsts.ERR400, "입력값에 대한 유효성 검사를 실패했습니다.");
		}
		
		map.put("articleId", articleId);
		map.put("tags", tags);

		if (content != null) {
			map.put("content", content);
		}
		
		if (file != null && file.isEmpty()) {
			map.put("file", file);
		}
		
		articleService.modifyArticle(map);

		return CommonResponse.ok();
	}
	
	@Operation(summary = "여행 후기 삭제")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "여행 후기 삭제 성공"),
	})
	@DeleteMapping("/delete/{articleid}")
	public CommonResponse<?> deleteArticle(@PathVariable("articleid") int articleId) throws Exception {
		articleService.deleteArticle(articleId);
		
		return CommonResponse.ok();
	}

}
