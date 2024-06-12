package com.ssafy.firskorea.attraction.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "MemberContentDto", description = "회원의 관광지에 대한 상태를 알아야 할 때 사용 ex) 북마크")
public class MemberContentDto {
    @Schema(required = true, description = "회원 아이디")
    private String memberId;
    @Schema(required = true, description = "관광지 아이디")
    private String contentId;
}
