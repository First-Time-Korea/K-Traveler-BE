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
@AllArgsConstructor
@Setter
@Getter
@ToString
@Schema(title = "LoginDto : 로그인 정보", description = "로그인을 위한 정보")
public class LoginDto extends MemberDto {
	
    @Schema(description = "회원 아이디")
    @NotBlank
    private String id;
    
    @Schema(description = "회원 비밀번호")
    @NotBlank
    private String password;
}
