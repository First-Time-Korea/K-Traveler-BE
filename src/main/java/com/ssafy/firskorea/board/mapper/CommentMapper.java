package com.ssafy.firskorea.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.firskorea.board.dto.CommentDto;

@Mapper
public interface CommentMapper {
	
	void writeComment(CommentDto comment) throws Exception;
	
	void deleteComment(int commentId) throws Exception;

}
