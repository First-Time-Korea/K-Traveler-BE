package com.ssafy.firskorea.board.dto.request;

import org.apache.ibatis.type.Alias;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@Alias("BoardSearch")
@Schema(title = "SearchDto: 여행 후기 리스트 검색 포맷")
public class SearchDto {
	
	@Schema(description = "검색 기준")
	private String key;
	
	@Schema(description = "검색어")
	private String word;
	
	@Schema(description = "페이지 번호")
	private int pgNo;
	
}
