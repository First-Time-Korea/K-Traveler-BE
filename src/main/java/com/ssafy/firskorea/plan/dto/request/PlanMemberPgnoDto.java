package com.ssafy.firskorea.plan.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Schema(title = "MemberPgnoDto", description = "회원의 북마크된 관광지 목록을 조회할 때 페이지네이션")
public class PlanMemberPgnoDto {
    @NotNull(message = "회원 아이디는 필수입니다.")
    @Size(min = 1, max = 20, message = "회원 ID는 최소 {min}자 이상, 최대 {max}자 이하이어야 합니다.")
    @Schema(required = true, description = "회원 아이디")
    private String memberId;

    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "페이지 번호는 숫자여야 합니다.")
    @Schema(description = "페이지 번호")
    private String pgno;
}