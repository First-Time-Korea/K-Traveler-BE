package com.ssafy.firskorea.domain.board.service;

import java.util.List;

import com.ssafy.firskorea.domain.board.dto.CommentDto;

public interface CommentService {
	
	List<CommentDto> writeComment(CommentDto comment) throws Exception;
	
	void deleteComment(int commentId) throws Exception;

}
