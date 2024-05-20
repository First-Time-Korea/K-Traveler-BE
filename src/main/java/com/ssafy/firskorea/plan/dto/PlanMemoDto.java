package com.ssafy.firskorea.plan.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PlanMemoDto {
	private int planAndAttractionId;
	private String text;
	private String modifiedTime;
}
