package com.ssafy.firskorea.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.firskorea.board.dto.CommentDto;
import com.ssafy.firskorea.board.service.CommentService;
import com.ssafy.firskorea.common.dto.CommonResponse;
import com.ssafy.firskorea.util.CommentStratify;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "여행 후기 댓글 컨트롤러")
@CrossOrigin(origins = "*")
@RestController
@Validated
@RequestMapping("/articles")
public class CommentController {

	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		super();
		this.commentService = commentService;
	}

	@Operation(summary = "여행 후기 댓글 작성")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "여행 후기 댓글 작성 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
			@ApiResponse(responseCode = "401", description = "회원 인증 실패"),
			@ApiResponse(responseCode = "403", description = "접근 권한 없음"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@PostMapping("/{articleid}/comments")
	public ResponseEntity<CommonResponse<?>> writeComment(@RequestBody @Valid CommentDto comment) throws Exception {
		List<CommentDto> comments = commentService.writeComment(comment);
		if (comments != null) {
			CommentStratify.stratify(comments);
		}
		
		CommonResponse<?> response = CommonResponse.okCreation(comments);
		
		ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(201).body(response);

		return responseEntity;
	}

	@Operation(summary = "여행 후기 댓글 삭제")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "여행 후기 댓글 삭제 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
			@ApiResponse(responseCode = "401", description = "회원 인증 실패"),
			@ApiResponse(responseCode = "403", description = "접근 권한 없음"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@Parameter(name = "commentid", description = "여행 후기 댓글 ID")
	@DeleteMapping("/{articleid}/comments/{commentid}")
	public ResponseEntity<CommonResponse<?>> deleteComment(@PathVariable("commentid") @Positive int commentId) throws Exception {
		commentService.deleteComment(commentId);
		
		CommonResponse<?> response = CommonResponse.ok();
		
		ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(200).body(response);

		return responseEntity;
	}

}
