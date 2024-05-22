package com.ssafy.firskorea.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CommentDto {
	
	private int id;
	private int articleId;
	private String memberId;
	private int parentCommentId;
	private int depth;
	private String content;
	private String createdTime;
	private boolean existed;
	private boolean existedOfMember;
	
	public boolean getExisted() {
		return existed;
	}
	
	public boolean getExistedOfMember() {
		return existedOfMember;
	}
	
}
