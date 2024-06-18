package com.ssafy.firskorea.plan.dto.request;

import java.util.List;

import com.ssafy.firskorea.plan.dto.PlanThumbnailDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PlanCreationDto {
	private String memberId;
	private String title;
	private PlanThumbnailDto planThumbnailDto;
	private List<PlanDateAttractionDto> planDateAttractionDtos;

	PlanCreationDto(String memberId, String title, List<PlanDateAttractionDto> planDateAttractionDtos) {
		this.memberId = memberId;
		this.title = title;
		this.planDateAttractionDtos = planDateAttractionDtos;
	}
}
