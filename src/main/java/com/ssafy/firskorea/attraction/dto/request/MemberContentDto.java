package com.ssafy.firskorea.attraction.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(title = "MemberContentDto", description = "회원의 관광지에 대한 상태를 알아야 할 때 사용 ex) 북마크")
public class MemberContentDto {

    @NotNull(message = "회원 ID는 필수입니다.")
    @Size(min = 1, max = 20, message = "회원 ID는 최소 {min}자 이상, 최대 {max}자 이하이어야 합니다.")
    @Schema(required = true, description = "회원 아이디")
    private String memberId;

    @NotNull(message = "관광지 ID는 필수입니다.")
    @Pattern(regexp ="^\\d+$", message = "관광지 ID 형식이 올바르지 않습니다.")
    @Schema(required = true, description = "관광지 아이디")
    private String contentId;

}