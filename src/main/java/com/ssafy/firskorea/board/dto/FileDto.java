package com.ssafy.firskorea.board.dto;

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
public class FileDto {
	
	private int id;
	private int articleId;
	private String saveFolder;
	private String originFile;
	private String saveFile;

}
