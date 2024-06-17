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

	/*
	 * 여행 후기 ID, 사용자 ID, 상위 댓글 ID 그리고 내용을 토대로 여행 후기를 생성한다.
	 * 
	 * @param {@link CommentDto} 객체로 여행 후기 댓글 작성을 위한 정보를 포함하는 전송 객체다.
	 * @return 새로 생성된 댓글을 포함한 해당 여행 후기의 댓글 리스트인 {@link CommentDto} 객체의 리스트를 반환한다.
	 */
	@Override
	@Transactional
	public List<CommentDto> writeComment(CommentDto comment) throws Exception {
		commentMapper.writeComment(comment);
		
		return commentMapper.getComments(comment.getArticleId());
	}

	/*
	 * 여행 후기 댓글 ID에 해당하는 여행 후기 댓글을 삭제한다.
	 * 
	 * @param commentId 여행 후기 댓글의 식별자다.
	 */
	@Override
	public void deleteComment(int commentId) throws Exception {
		commentMapper.deleteComment(commentId);
	}

}
