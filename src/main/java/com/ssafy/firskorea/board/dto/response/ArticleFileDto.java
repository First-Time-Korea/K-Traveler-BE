package com.ssafy.firskorea.board.dto.response;

import java.util.Map;

import com.ssafy.firskorea.board.dto.FileDto;

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
public class ArticleFileDto {
	
	private int articleId;
	private String memberId;
	private Map<String, String> img;

}
