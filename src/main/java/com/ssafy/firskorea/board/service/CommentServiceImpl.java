package com.ssafy.firskorea.board.service;

import org.springframework.stereotype.Service;

import com.ssafy.firskorea.board.dto.CommentDto;
import com.ssafy.firskorea.board.mapper.CommentMapper;

@Service
public class CommentServiceImpl implements CommentService {
	
	private CommentMapper commentMapper;

	public CommentServiceImpl(CommentMapper commentMapper) {
		super();
		this.commentMapper = commentMapper;
	}

	@Override
	public void writeComment(CommentDto comment) throws Exception {
		commentMapper.writeComment(comment);
	}

	@Override
	public void deleteComment(int commentId) throws Exception {
		commentMapper.deleteComment(commentId);
	}

}
