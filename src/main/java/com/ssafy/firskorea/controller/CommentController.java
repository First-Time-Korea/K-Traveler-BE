package com.ssafy.firskorea.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.firskorea.board.dto.CommentDto;
import com.ssafy.firskorea.board.service.CommentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/comment")
public class CommentController {
	
	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		super();
		this.commentService = commentService;
	}
	
	// 여행 후기 댓글 작성하기
	@PostMapping("/write")
	public ResponseEntity<Map<String, Object>> postMethodName(@RequestBody CommentDto comment) throws Exception {
		commentService.writeComment(comment);
		
		Map<String, Object> response = new HashMap<>();
		response.put("message", "여행 후기 댓글 작성 성공");

		ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.status(201).body(response);

		return responseEntity;
	}
	
	// 여행 후기 댓글 삭제하기
	@DeleteMapping("/delete/{commentid}")
	public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable("commentid") int commentId) throws Exception {
		commentService.deleteComment(commentId);
		
		Map<String, Object> response = new HashMap<>();
		response.put("message", "여행 후기 댓글 삭제 성공");

		ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.status(200).body(response);

		return responseEntity;
	}

}
