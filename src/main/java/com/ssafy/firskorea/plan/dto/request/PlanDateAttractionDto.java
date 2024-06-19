package com.ssafy.firskorea.plan.dto.request;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Schema(title = "PlanDateAttractionDto", description = "여행의 특정 날짜에 방문할 관광지 모음")
public class PlanDateAttractionDto {
	@NotNull()
	@Schema(description = "여행 계획에 포함 되는 날짜")
	private LocalDateTime date;
	@NotNull()
	@Size(min = 1, message = "한 개 이상의 관광지를 방문 해야 합니다.")
	@Schema(description = "해당 날짜에 방문할 관광지 모음")
	private String[] contentId;
}
