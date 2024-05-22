package com.ssafy.firskorea.plan.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AttractionForPlan {
	// plan이랑 PlanAndAttraction 이랑 조인해서 가져옴
	// 필요한 정보들
	private int paaId;
	private LocalDateTime date;
	private String contentTitle;
	private int contentId;
	private String addr1;
	private String firstImage;
	private double longitude;
	private double latitude;
	private String memoText;
}
