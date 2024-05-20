package com.ssafy.firskorea.plan.dto.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AttractionPerDate {
	private LocalDateTime date;
	private String[] contentId;
}
