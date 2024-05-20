package com.ssafy.firskorea.plan.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PlanDto {
	private String memberId;
	private String title;
	private String createdTime;
	private String modifiedTime;
}
