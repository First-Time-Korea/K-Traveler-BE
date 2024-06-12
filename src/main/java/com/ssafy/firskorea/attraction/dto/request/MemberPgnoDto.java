package com.ssafy.firskorea.attraction.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(title = "MemberPgnoDto", description = "회원의 북마크된 관광지 목록을 조회할 때 페이지네이션")
public class MemberPgnoDto {
    @Schema(required = true, description = "회원 아이디")
    private String memberId;
    @Schema(description = "페이지 번호")
    private String pgno;
}
