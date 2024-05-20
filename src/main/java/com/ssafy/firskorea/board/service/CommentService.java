package com.ssafy.firskorea.board.service;

import com.ssafy.firskorea.board.dto.CommentDto;

public interface CommentService {
	
	void writeComment(CommentDto comment) throws Exception;
	
	void deleteComment(int commentId) throws Exception;

}
