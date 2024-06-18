package com.ssafy.firskorea.attraction.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "SearchDto", description = "조건으로 목록을 필터링 할 때 사용")
public class SearchDto {
    @Size(min = 1, max = 20, message = "회원 ID는 최소 {min}자 이상, 최대 {max}자 이하이어야 합니다.")
    @Schema(description = "회원 아이디", nullable = true) // nullable 속성 추가
    private String memberId;

    @Schema(description = "검색 키워드", nullable = true)
    private String keyword;

    @NotNull
    @Pattern(regexp = "^\\d+$", message = "지역 시도 코드 형식이 올바르지 않습니다.")
    @Schema(description = "지역 시도 코드", required = true, defaultValue = "1")
    private String sidoCode;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]$", message = "테마 코드 형식이 올바르지 않습니다.")
    @Schema(description = "테마 코드", required = true, defaultValue = "A")
    private String themeCode;

    @Pattern(regexp = "^[a-zA-Z]\\d{2}$", message = "카테고리 코드 형식이 올바르지 않습니다.")
    @Schema(description = "테마의 하위 카테고리 코드", nullable = true)
    private String categoryCode;

    @Pattern(regexp = "^\\d+$", message = "관광지 아이디 형식이 올바르지 않습니다.")
    @Schema(description = "관광지 아이디", nullable = true)
    private String contentId;

}
