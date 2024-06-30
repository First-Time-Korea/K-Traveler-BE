package com.ssafy.firskorea.domain.board.dto.response;

import java.util.Map;

<<<<<<< HEAD:src/main/java/com/ssafy/firskorea/domain/board/dto/response/ArticleFileDto.java
import lombok.AllArgsConstructor;
=======
>>>>>>> f4c0c53374281df1649e97304c6b66c405f70406:src/main/java/com/ssafy/firskorea/board/dto/response/ArticleFileDto.java
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class ArticleFileDto {
	
	private int articleId;
	private String memberId;
	private Map<String, String> img;

}
