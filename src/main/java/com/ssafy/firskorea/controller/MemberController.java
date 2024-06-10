package com.ssafy.firskorea.controller;

import java.util.Map;

import com.ssafy.firskorea.common.consts.RetConsts;
import com.ssafy.firskorea.common.dto.ApiResponse;
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
@Tag(name = "회원 인증 컨트롤러", description = "로그인 로그아웃, 토큰처리등 회원의 인증관련 처리하는 클래스.")
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

    @PostMapping("/signup")
    public ApiResponse<?> signup(@RequestBody MemberDto memberDto) throws Exception {
        memberService.signUp(memberDto);
        return ApiResponse.ok();
    }

    @GetMapping("/signup")
    public ApiResponse<?> idDuplicationCheck(@RequestParam String id) throws Exception {
        if (!memberService.idCheck(id)) {
            return ApiResponse.failure(RetConsts.ERR603, "중복된 아이디가 존재합니다.");
        }
        return ApiResponse.ok();
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody MemberDto memberDto) throws Exception {
        MemberDto loginUser = memberService.login(memberDto);
        if (loginUser == null) {
            return ApiResponse.failure(RetConsts.ERR602, "아이디 또는 패스워드를 확인해 주세요.");
        }
        String accessToken = jwtUtil.createAccessToken(loginUser.getId());
        String refreshToken = jwtUtil.createRefreshToken(loginUser.getId());
        memberService.saveRefreshToken(loginUser.getId(), refreshToken); //	발급받은 refresh token 을 DB에 저장.
        return ApiResponse.ok(Map.of(
                "access-token", accessToken,
                "refresh-token", refreshToken
        ));
    }

    @Operation(summary = "회원인증", description = "회원 정보를 담은 Token 을 반환한다.")
    @GetMapping("/info/{userId}")
    public ApiResponse<?> getInfo(
            @PathVariable("userId") @Parameter(description = "인증할 회원의 아이디.", required = true) String userId,
            HttpServletRequest request) throws Exception {
        if (!jwtUtil.checkToken(request.getHeader("Authorization"))) {
            return ApiResponse.failure(RetConsts.ERR401, "토큰 사용 불가");
        }
        return ApiResponse.ok(memberService.userInfo(userId));
    }

    @Operation(summary = "로그아웃", description = "회원 정보를 담은 Token 을 제거한다.")
    @GetMapping("/logout/{userId}")
    @Hidden
    public ApiResponse<?> removeToken(
            @PathVariable("userId") @Parameter(description = "로그아웃 할 회원의 아이디.", required = true) String userId) throws Exception {
        memberService.deleRefreshToken(userId);
        return ApiResponse.ok();
    }

    @Operation(summary = "Access Token 재발급", description = "만료된 access token 을 재발급 받는다.")
    @PostMapping("/refresh")
    public ApiResponse<?> refreshToken(@RequestBody MemberDto memberDto, HttpServletRequest request) throws Exception {
        String headerRefreshToken = request.getHeader("refreshToken");
        if (!jwtUtil.checkToken(headerRefreshToken) || (headerRefreshToken.equals(memberService.getRefreshToken(memberDto.getId())))) {
            return ApiResponse.failure(RetConsts.ERR401, "토큰 사용 불가");
        }
        String accessToken = jwtUtil.createAccessToken(memberDto.getId());
        return ApiResponse.ok(accessToken);
    }

}