package com.ssafy.firskorea.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.firskorea.board.dto.CommentDto;
import com.ssafy.firskorea.board.service.CommentService;
import com.ssafy.firskorea.common.consts.RetConsts;
import com.ssafy.firskorea.common.dto.CommonResponse;
import com.ssafy.firskorea.util.CommentStratify;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "여행 후기 댓글 컨트롤러")
@CrossOrigin(origins = "*")
@RestController
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
			@ApiResponse(responseCode = "401", description = "유효하지 않은 회원 아이디")
	})
	@PostMapping("/{articleid}/comments/write")
	public CommonResponse<?> writeComment(@RequestBody CommentDto comment) throws Exception {
		// 입력값 유효성 검사하기
		if (comment.getArticleId() == 0 || comment.getMemberId() == null || comment.getMemberId().equals("")
				|| comment.getContent() == null || comment.getContent().equals("")) {
			return CommonResponse.failure(RetConsts.ERR400, "입력값에 대한 유효성 검사를 실패했습니다.");
		}
		
		List<CommentDto> comments = commentService.writeComment(comment);
		if (comments != null) {
			CommentStratify.stratify(comments);
		}

		return CommonResponse.okCreation(comments);
	}

	@Operation(summary = "여행 후기 댓글 삭제")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "여행 후기 댓글 삭제 성공")
	})
	@Parameter(name = "commentid", description = "여행 후기 댓글 ID")
	@DeleteMapping("/{articleid}/comments/delete/{commentid}")
	public CommonResponse<?> deleteComment(@PathVariable("commentid") int commentId) throws Exception {
		commentService.deleteComment(commentId);

		return CommonResponse.ok();
	}

}
