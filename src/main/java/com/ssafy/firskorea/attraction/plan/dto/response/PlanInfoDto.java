package com.ssafy.firskorea.attraction.plan.dto.response;

import com.ssafy.firskorea.attraction.plan.dto.request.PlanThumbnailDto;
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
