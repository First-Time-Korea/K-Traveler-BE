package com.ssafy.firskorea.controller;

import java.util.Map;

import com.ssafy.firskorea.common.consts.RetConsts;
import com.ssafy.firskorea.common.dto.CommonResponse;
import com.ssafy.firskorea.member.dto.request.LoginDto;
import com.ssafy.firskorea.member.dto.request.RegistrationDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ssafy.firskorea.member.dto.MemberDto;
import com.ssafy.firskorea.member.service.MemberServiceImpl;
import com.ssafy.firskorea.util.JWTUtil;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@Tag(name = "회원 컨트롤러")
@Slf4j
public class MemberController {
    private final MemberServiceImpl memberService;
    private final JWTUtil jwtUtil;

    @Autowired
    public MemberController(MemberServiceImpl memberService, JWTUtil jwtUtil) {
        super();
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "회원 가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 가입 성공"),
            @ApiResponse(responseCode = "600", description = "회원 가입 실패"),
    })
    @PostMapping("/signup")
    public CommonResponse<?> signup(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "등록할 회원 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegistrationDto.class)))
            @org.springframework.web.bind.annotation.RequestBody RegistrationDto memberDto) throws Exception {
        memberService.signUp(memberDto);
        return CommonResponse.ok();
    }

    @Operation(summary = "아이디 중복 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 중복 검사 완료"),
            @ApiResponse(responseCode = "603", description = "중복 아이디 존재"),
            @ApiResponse(responseCode = "600", description = "중복 검사 중 오류 발생"),
    })
    @GetMapping("/check-id")
    public CommonResponse<?> idDuplicationCheck(
            @Parameter(description = "중복 검사할 아이디.", required = true)
            @RequestParam String id) throws Exception {
        if (!memberService.idCheck(id)) {
            return CommonResponse.failure(RetConsts.ERR603, "중복된 아이디가 존재합니다.");
        }
        return CommonResponse.ok();
    }

    @Operation(summary = "로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "602", description = "회원 정보 불일치"),
            @ApiResponse(responseCode = "600", description = "로그인 중 오류 발생"),
    })
    @PostMapping("/login")
    public CommonResponse<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "로그인 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginDto.class)))
            @org.springframework.web.bind.annotation.RequestBody LoginDto memberDto) throws Exception {

        MemberDto loginUser = memberService.login(memberDto);
        if (loginUser == null) {
            return CommonResponse.failure(RetConsts.ERR602, "아이디 또는 패스워드를 확인해 주세요.");
        }
        String accessToken = jwtUtil.createAccessToken(loginUser.getId());
        String refreshToken = jwtUtil.createRefreshToken(loginUser.getId());
        memberService.saveRefreshToken(loginUser.getId(), refreshToken); //	발급받은 refresh token 을 DB에 저장.
        return CommonResponse.ok(Map.of(
                "access-token", accessToken,
                "refresh-token", refreshToken
        ));
    }

    @Operation(summary = "로그아웃", description = "회원 정보를 담은 Token 을 제거한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "600", description = "로그아웃 실패"),
    })
    @GetMapping("/logout/{userId}")
    public CommonResponse<?> removeToken(
            @Parameter(description = "로그아웃 할 회원의 아이디.", required = true)
            @PathVariable String userId) throws Exception {
        memberService.deleRefreshToken(userId);
        return CommonResponse.ok();
    }

    @Operation(summary = "회원 인증", description = "access token으로 유효한 회원인지 인증한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 인증 성공"),
            @ApiResponse(responseCode = "401", description = "토큰 사용 불가"),
            @ApiResponse(responseCode = "600", description = "회원 인증 실패"),
    })
    @Hidden
    @GetMapping("/info/{userId}")
    public CommonResponse<?> getInfo(
            @Parameter(description = "인증할 회원의 아이디.", required = true)
            @PathVariable String userId,
            HttpServletRequest request) throws Exception {
        if (!jwtUtil.checkToken(request.getHeader("Authorization"))) {
            return CommonResponse.failure(RetConsts.ERR401, "토큰 사용 불가");
        }
        return CommonResponse.ok(memberService.userInfo(userId));
    }

    @Operation(summary = "Access Token 재발급", description = "refresh token으로 만료된 access token 을 재발급 받는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "401", description = "토큰 사용 불가"),
            @ApiResponse(responseCode = "600", description = "토큰 재발급 실패"),
    })
    @Hidden
    @PostMapping("/refresh/{userId}")
    public CommonResponse<?> refreshToken(
            @Parameter(description = "재발급 받을 아이디.", required = true)
            @PathVariable String userId,
            HttpServletRequest request) throws Exception {
        String headerRefreshToken = request.getHeader("refreshToken");
        if (!jwtUtil.checkToken(headerRefreshToken) || (headerRefreshToken.equals(memberService.getRefreshToken(userId)))) {
            return CommonResponse.failure(RetConsts.ERR401, "토큰 사용 불가");
        }
        String accessToken = jwtUtil.createAccessToken(userId);
        return CommonResponse.ok(accessToken);
    }

}