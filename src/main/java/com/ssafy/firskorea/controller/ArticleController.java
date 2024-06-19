package com.ssafy.firskorea.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.firskorea.board.dto.ArticleDto;
import com.ssafy.firskorea.board.dto.request.SearchDto;
import com.ssafy.firskorea.board.dto.response.ArticleAndCommentDto;
import com.ssafy.firskorea.board.service.ArticleService;
import com.ssafy.firskorea.common.dto.CommonResponse;
import com.ssafy.firskorea.util.CommentStratify;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "여행 후기 컨트롤러")
@CrossOrigin(origins = "*")
@RestController
@Validated
@RequestMapping("/articles")
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
			@ApiResponse(responseCode = "401", description = "회원 인증 실패"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@Parameters({
		@Parameter(name = "userid", description = "사용자 아이디"),
		@Parameter(name = "tags", description = "여행 후기 태그"),
		@Parameter(name = "content", description = "여행 후기 본문"),
		@Parameter(name = "file", description = "여행 후기 사진")
	})
	@PostMapping("")
	public ResponseEntity<CommonResponse<?>> writeArticle(@RequestParam("userid") @NotBlank String userId,
			@RequestParam("tags") @NotEmpty List<String> tags,
			@RequestParam(value = "content", required = false) String content,
			@RequestParam("file") @NotNull MultipartFile file) throws Exception {
		Map<String, Object> map = new HashMap<>();
		
		map.put("userId", userId);
		map.put("tags", tags);
		map.put("file", file);
		
		if (content == null) {
			map.put("content", "");
		} else {
			map.put("content", content);
		}
		
		articleService.writeArticle(map);
		
		CommonResponse<?> response = CommonResponse.okCreation();

		ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(201).body(response);
		
		return responseEntity;
	}

	@Operation(summary = "여행 후기 리스트 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "여행 후기 리스트 조회 성공"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping("")
	public ResponseEntity<CommonResponse<?>> getArticles(@ModelAttribute SearchDto search) throws Exception {
		Map<String, Object> result = articleService.getArticles(search);

		Map<String, Object> responseData = new HashMap<>();
		responseData.put("articles", result.get("articleFiles"));
		responseData.put("currentPage", result.get("currentPage"));
		responseData.put("totalPageCount", result.get("totalPageCount"));
		
		CommonResponse<?> response = CommonResponse.ok(responseData);
		
		ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(200).body(response);

		return responseEntity;
	}
	
	@Operation(summary = "여행 후기 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "여행 후기 조회 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@Parameter(name = "articleid", description = "여행 후기 ID")
	@GetMapping("/{articleid}")
	public ResponseEntity<CommonResponse<?>> getArticle(@PathVariable("articleid") @Positive int articleId) throws Exception {
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
		
		CommonResponse<?> response = CommonResponse.ok(responseData);
		
		ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(200).body(response);

		return responseEntity;
	}
	
	@Operation(summary = "여행 후기 조회 for 수정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "여행 후기 조회 for 수정 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
			@ApiResponse(responseCode = "401", description = "회원 인증 실패"),
			@ApiResponse(responseCode = "403", description = "접근 권한 없음"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@Parameter(name = "articleid", description = "여행 후기 ID")
	@GetMapping("/modify/{articleid}")
	public ResponseEntity<CommonResponse<?>> getArticleForModification(@PathVariable("articleid") @Positive int articleId) throws Exception {
		ArticleDto article = articleService.getArticleForModification(articleId);
		
		Map<String, Object> responseData = new HashMap<>();
		responseData.put("article", article);
		
		CommonResponse<?> response = CommonResponse.ok(responseData);
		
		ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(200).body(response);

		return responseEntity;
	}
	
	@Operation(summary = "여행 후기 수정")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "여행 후기 수정 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
			@ApiResponse(responseCode = "401", description = "회원 인증 실패"),
			@ApiResponse(responseCode = "403", description = "접근 권한 없음"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@Parameters({
		@Parameter(name = "articleid", description = "여행 후기 ID"),
		@Parameter(name = "tags", description = "여행 후기 태그"),
		@Parameter(name = "content", description = "여행 후기 본문"),
		@Parameter(name = "file", description = "여행 후기 사진")
	})
	@PutMapping("/{articleid}")
	public ResponseEntity<CommonResponse<?>> modifyArticle(@PathVariable("articleid") @Positive int articleId,
			@RequestParam("tags") @NotEmpty List<String> tags, @RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		Map<String, Object> map = new HashMap<>();
		
		map.put("articleId", articleId);
		map.put("tags", tags);

		if (content != null) {
			map.put("content", content);
		}
		
		if (file != null && file.isEmpty()) {
			map.put("file", file);
		}
		
		articleService.modifyArticle(map);
		
		CommonResponse<?> response = CommonResponse.ok();
		
		ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(200).body(response);

		return responseEntity;
	}
	
	@Operation(summary = "여행 후기 삭제")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "여행 후기 삭제 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
			@ApiResponse(responseCode = "401", description = "회원 인증 실패"),
			@ApiResponse(responseCode = "403", description = "접근 권한 없음"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@Parameter(name = "articleid", description = "여행 후기 ID")
	@DeleteMapping("/{articleid}")
	public ResponseEntity<CommonResponse<?>> deleteArticle(@PathVariable("articleid") @Positive int articleId) throws Exception {
		articleService.deleteArticle(articleId);
		
		CommonResponse<?> response = CommonResponse.ok();
		
		ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(200).body(response);

		return responseEntity;
	}

}
