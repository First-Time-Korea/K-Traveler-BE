package com.ssafy.firskorea.attraction.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SearchDto {
	private String keyword;
	private Character themeCode;
	private String categoryCode;
}
