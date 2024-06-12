package com.ssafy.firskorea.attraction.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(title = "SidoPgnoDto", description = "해당 지역의 전체 관광지 목록을 조회할 때 페이지네이션")
public class SidoPgnoDto {
    @Schema(required = true, description = "지역 시도 코드")
    private String sidocode;
    @Schema(description = "페이지 번호")
    private String pgno;
}
