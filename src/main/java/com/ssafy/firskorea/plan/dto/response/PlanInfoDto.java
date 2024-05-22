package com.ssafy.firskorea.plan.dto.response;

import java.util.List;

import com.ssafy.firskorea.board.dto.FileDto;
import com.ssafy.firskorea.board.dto.TagDto;
import com.ssafy.firskorea.plan.dto.PlanFileDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class PlanInfoDto {
	
	private int id;
	private String title;
	private String startDate;
	private String endDate;
	private PlanFileDto file;
	
}
