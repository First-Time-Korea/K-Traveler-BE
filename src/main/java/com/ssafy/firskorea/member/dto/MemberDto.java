package com.ssafy.firskorea.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class MemberDto {
    private String id;
    private String name;
    @JsonIgnore
    private String password;
    private String emailId;
    private String emailDomain;
    private String status;
    private String joinDate;
    private String refreshToken;

}
