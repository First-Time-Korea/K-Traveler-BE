package com.ssafy.firskorea.plan.dto.response;

import java.util.List;
import java.util.Map;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlanDetailsDto {
	private int planId;
	private String planTitle;
	private Map<String, List<PlanAttractionDetailsDto>> attractions;

	public PlanDetailsDto(int planId, Map<String, List<PlanAttractionDetailsDto>> attractions) {
		this.planId = planId;
		this.attractions=attractions;
	}

}
