package com.ssafy.firskorea.plan.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PlanAttractionDetailsDto {
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
