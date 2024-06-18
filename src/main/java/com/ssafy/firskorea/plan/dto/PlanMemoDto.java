package com.ssafy.firskorea.plan.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PlanMemoDto {
	private int planAndAttractionId;
	private String text;
}
