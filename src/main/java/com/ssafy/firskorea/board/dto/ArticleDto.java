package com.ssafy.firskorea.board.dto;

import java.util.List;

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
public class ArticleDto {
	
	private int id;
	private String memberId;
	private List<TagDto> tags;
	private String content;
	private int hit;
	private String createdTime;
	private String modifiedTime;
	private FileDto file;

}
