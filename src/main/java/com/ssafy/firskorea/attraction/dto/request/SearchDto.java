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
	private String themeCode;
	private String categoryCode;
	private String title; // 관광지명
}
