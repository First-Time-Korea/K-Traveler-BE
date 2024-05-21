package com.ssafy.firskorea.board.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public List<CommentDto> writeComment(CommentDto comment) throws Exception {
		commentMapper.writeComment(comment);
		
		return commentMapper.getComments(comment.getArticleId());
	}

	@Override
	public void deleteComment(int commentId) throws Exception {
		commentMapper.deleteComment(commentId);
	}

}
