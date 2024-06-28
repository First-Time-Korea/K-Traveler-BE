package com.ssafy.firskorea.member.dto.request;

import com.ssafy.firskorea.member.dto.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@Schema(title = "RegistrationDto : 회원 가입 정보", description = "회원 가입을 위한 정보")
public class RegistrationDto extends MemberDto {
	
    @Schema(description = "회원 아이디")
    @NotBlank
    private String id;
    
    @Schema(description = "회원 이름")
    @NotBlank
    private String name;
    
    @Schema(description = "회원 비밀번호")
    @NotBlank
    private String password;
    
    @Schema(description = "이메일 아이디")
    @NotBlank
    private String emailId;
    
    @Schema(description = "이메일 도메인")
    @NotBlank
    private String emailDomain;
}