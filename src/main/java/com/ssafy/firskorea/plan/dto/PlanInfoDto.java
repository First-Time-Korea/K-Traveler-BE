package com.ssafy.firskorea.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class PlanInfoDto {
	private int id;
	private String title;
	private String startDate;
	private String endDate;
	private PlanThumbnailDto file;
	
}
