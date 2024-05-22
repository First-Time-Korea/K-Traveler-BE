package com.ssafy.firskorea.attraction.dto.response;

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
public class BookmarkedAttractionInfoDto {
	
	private int bookmarkId;
	private String contentId;
	private String title;
	private String firstImage;
	private String firstImage2;

}
