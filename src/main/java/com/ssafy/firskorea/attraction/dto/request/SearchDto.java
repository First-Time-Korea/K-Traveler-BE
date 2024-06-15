package com.ssafy.firskorea.attraction.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "SearchDto", description = "조건으로 목록을 필터링 할 때 사용")
public class SearchDto {
    @Schema(description = "회원 아이디", nullable = true) // nullable 속성 추가
    private String memberId;

    @Schema(description = "검색 키워드", nullable = true)
    private String keyword;

    @Schema(description = "지역 시도 코드", required = true, defaultValue = "1")
    private int sidoCode;

    @Schema(description = "테마  코드", required = true, defaultValue = "A")
    private Character themeCode;

    @Schema(description = "테마의 하위 카테고리 코드", nullable = true)
    private String categoryCode;

    @Schema(description = "관광지 아이디", nullable = true)
    private int contentId;
}
