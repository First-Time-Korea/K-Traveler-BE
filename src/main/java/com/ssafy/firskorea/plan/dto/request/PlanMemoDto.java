package com.ssafy.firskorea.plan.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Schema(title = "PlanMemoDto", description = "여행 계획에 등록할 메모 데이터")
public class PlanMemoDto {
    @NotNull
    @Schema(required = true, description = "날짜-관광지 연결 테이블 아이디")
    private int planAndAttractionId;
    @NotNull
    @Schema(required = true, description = "메모 텍스트")
    private String text;
}
