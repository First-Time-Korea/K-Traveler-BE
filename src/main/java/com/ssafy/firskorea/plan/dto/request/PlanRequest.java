package com.ssafy.firskorea.plan.dto.request;

import java.util.List;

import com.ssafy.firskorea.plan.dto.PlanFileDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PlanRequest {
	private String memberId;
	private String title;
	private PlanFileDto planFileDto;
	private List<AttractionPerDate> attractionsPerDate;

	PlanRequest(String memberId, String title, List<AttractionPerDate> attractionsPerDate) {
		this.memberId = memberId;
		this.title = title;
		this.attractionsPerDate = attractionsPerDate;
	}
}
