package com.ssafy.firskorea.domain.board.dto.response;

import java.util.Map;

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
