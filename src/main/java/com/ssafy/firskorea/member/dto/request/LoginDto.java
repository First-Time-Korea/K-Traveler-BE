package com.ssafy.firskorea.member.dto.request;

import com.ssafy.firskorea.member.dto.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "LoginDto : 로그인 정보", description = "로그인을 위한 정보")
public class LoginDto extends MemberDto {
    @Schema(required = true, description = "회원 아이디")
    private String id;
    @Schema(required = true, description = "회원 비밀번호")
    private String password;
}
