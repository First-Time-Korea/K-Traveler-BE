package com.ssafy.firskorea.plan.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@Setter
@Schema(title = "PlanCreationDto", description = "여행 계획 생성에 필요한 데이터")
public class PlanCreationDto {

    @NotNull()
    @Size(min = 1, max = 20, message = "회원 ID는 최소 {min}자 이상, 최대 {max}자 이하이어야 합니다.")
    @Schema(required = true, description = "회원 아이디")
    private String memberId;

    @NotNull
    @Size(min = 5, message = "여행 계획은 최소 {min}자 이상 입력해 주세요.")
    @Schema(required = true, description = "여행 계획 제목")
    private String title;

    @Schema(description = "여행 계획 썸네일 정보")
    private PlanThumbnailDto planThumbnailDto;

    @NotNull()
    @Schema(required = true, description = "날짜별 방문할 관광지")
    private List<PlanDateAttractionDto> planDateAttractionDtos;

    PlanCreationDto(String memberId, String title, List<PlanDateAttractionDto> planDateAttractionDtos) {
        this.memberId = memberId;
        this.title = title;
        this.planDateAttractionDtos = planDateAttractionDtos;
    }
}
