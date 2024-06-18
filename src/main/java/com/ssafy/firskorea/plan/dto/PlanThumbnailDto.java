package com.ssafy.firskorea.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlanThumbnailDto {
	private String planId;
	private String saveFolder;
	private String originFile;
	private String saveFile;

	public PlanThumbnailDto(String saveFolder, String originFile, String saveFile) {
		this.saveFile = saveFile;
		this.saveFolder = saveFolder;
		this.originFile = originFile;
	}
}
