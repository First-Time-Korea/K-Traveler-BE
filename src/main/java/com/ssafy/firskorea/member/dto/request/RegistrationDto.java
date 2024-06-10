package com.ssafy.firskorea.member.dto.request;

import com.ssafy.firskorea.member.dto.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@Schema(title = "RegistrationDto : 회원 가입 정보", description = "회원 가입을 위한 정보")
public class RegistrationDto extends MemberDto {
    @Schema(required = true, description = "회원 아이디")
    private String id;
    @Schema(required = true, description = "회원 이름")
    private String name;
    @Schema(required = true, description = "회원 비밀번호")
    private String password;
    @Schema(required = true, description = "이메일 아이디")
    private String emailId;
    @Schema(required = true, description = "이메일 도메인")
    private String emailDomain;
}