package com.ssafy.firskorea.plan.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PlanAndAttractionDto {
	private int paaId;
	private String planTitle;
	private int contentId;
}
