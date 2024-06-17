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
import com.ssafy.firskorea.common.dto.CommonResponse;
import com.ssafy.firskorea.util.CommentStratify;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/article")
public class CommentController {
	
	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		super();
		this.commentService = commentService;
	}
	
	// 여행 후기 댓글 작성하기
	@PostMapping("/{articleid}/comment/write")
	public CommonResponse<?> postMethodName(@RequestBody CommentDto comment) throws Exception {
		List<CommentDto> comments = commentService.writeComment(comment);
		if (comments != null) {
			CommentStratify.stratify(comments);
		}

		return CommonResponse.okCreation(comments);
	}
	
	// 여행 후기 댓글 삭제하기
	@DeleteMapping("/{articleid}/comment/delete/{commentid}")
	public CommonResponse<?> deleteComment(@PathVariable("commentid") int commentId) throws Exception {
		commentService.deleteComment(commentId);
		
		return CommonResponse.ok();
	}

}
