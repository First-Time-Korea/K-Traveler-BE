package com.ssafy.firskorea.plan.dto.response;

import java.util.List;

import com.ssafy.firskorea.plan.dto.request.AttractionPerDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PlanResponse {
	private int planId;
	private String planTitle;
	private List<AttractionForPlan> attractions;

	public PlanResponse(int planId) {
		this.planId = planId;
	}
}
