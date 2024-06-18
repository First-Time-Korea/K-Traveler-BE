package com.ssafy.firskorea.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
@Schema(title = "CommetDto: 여행 후기 댓글 포맷")
public class CommentDto {
	
	@Schema(hidden = true)
	private int id;
	
	@Schema(description = "여행 후기 ID")
	@Positive
	@NotNull
	private int articleId;
	
	@Schema(description = "사용자 아이디")
	@NotBlank
	private String memberId;
	
	@Schema(description = "상위 여행 후기 댓글 ID")
	@Positive
	private int parentCommentId;

	@Schema(hidden = true)
	private int depth;
	
	@Schema(description = "여행 후기 본문")
	@NotBlank
	private String content;

	@Schema(hidden = true)
	private String createdTime;

	@Schema(hidden = true)
	private boolean existed;

	@Schema(hidden = true)
	private boolean existedOfMember;
	
	public boolean getExisted() {
		return existed;
	}
	
	public boolean getExistedOfMember() {
		return existedOfMember;
	}
	
}
