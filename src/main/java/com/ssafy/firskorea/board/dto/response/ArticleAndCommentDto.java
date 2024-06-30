package com.ssafy.firskorea.board.dto.response;

import java.util.List;

import com.ssafy.firskorea.board.dto.CommentDto;
import com.ssafy.firskorea.board.dto.FileDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class ArticleAndCommentDto {
	
	private int id;
	private String memberId;
	private boolean existedOfMember;
	private String content;
	private int hit;
	private String createdTime;
	private String modifiedTime;
	private FileDto file;
	private List<CommentDto> comments;

}
