package com.ssafy.firskorea.attraction.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(title = "SidoPgnoDto", description = "해당 지역의 전체 관광지 목록을 조회할 때 페이지네이션")
public class SidoPgnoDto {
    @NotNull(message = "지역 시도 코드는 필수입니다.")
    @Pattern(regexp = "^\\d+$", message = "지역 시도 코드 형식이 올바르지 않습니다.")
    @Schema(required = true, description = "지역 시도 코드")
    private String sidocode;

    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "페이지 번호는 숫자여야 합니다.")
    @Schema(description = "페이지 번호")
    private String pgno;

}
