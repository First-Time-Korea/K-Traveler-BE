package com.ssafy.firskorea.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.firskorea.board.dto.CommentDto;

@Mapper
public interface CommentMapper {
	
	void writeComment(CommentDto comment) throws Exception;
	
	List<CommentDto> getComments(int articleId) throws Exception;
	
	void deleteComment(int commentId) throws Exception;

}
