package com.ssafy.firskorea.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.firskorea.common.consts.RetConsts;
import com.ssafy.firskorea.common.dto.CommonResponse;
import com.ssafy.firskorea.common.exception.DuplicationMemberIdException;
import com.ssafy.firskorea.common.exception.IncorrectMemberException;
import com.ssafy.firskorea.common.exception.MemberAlreadyWithdrawnException;
import com.ssafy.firskorea.member.dto.MemberDto;
import com.ssafy.firskorea.member.dto.request.LoginDto;
import com.ssafy.firskorea.member.dto.request.RegistrationDto;
import com.ssafy.firskorea.member.service.MemberServiceImpl;
import com.ssafy.firskorea.util.JWTUtil;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "회원 컨트롤러")
@CrossOrigin(origins = "*")
@RestController
@Validated
@RequestMapping("/users")
public class MemberController {
	
    private final MemberServiceImpl memberService;
    private final JWTUtil jwtUtil;

    public MemberController(MemberServiceImpl memberService, JWTUtil jwtUtil) {
        super();
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "회원 가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 가입 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<?>> signup(@RequestBody @Valid RegistrationDto member) throws Exception {
        memberService.signUp(member);
        
        CommonResponse<?> response = CommonResponse.okCreation();
        
        ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(201).body(response);
        
        return responseEntity;
    }

    @Operation(summary = "아이디 중복 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 중복 검사 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
			@ApiResponse(responseCode = "409", description = "중복된 아이디"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "memberid", description = "회원 ID")
    @GetMapping("/check-id")
    public ResponseEntity<CommonResponse<?>> checkDuplicationMemberId(@RequestParam("memberid") @NotBlank String memberId) throws Exception {
        if (!memberService.checkDuplicationMemberId(memberId)) {
        	throw new DuplicationMemberIdException();
        }
        
        CommonResponse<?> response = CommonResponse.ok();
    	
    	ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(200).body(response);
        
        return responseEntity;
    }

    @Operation(summary = "로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
			@ApiResponse(responseCode = "401", description = "회원 인증 실패"),
			@ApiResponse(responseCode = "403", description = "탈퇴한 회원"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<?>> login(@RequestBody @Valid LoginDto member) throws Exception {
        MemberDto loginUser = memberService.login(member);
        
        if (loginUser == null) {
        	throw new IncorrectMemberException();
        } else if (!loginUser.isExistedOfMember()) {
        	throw new MemberAlreadyWithdrawnException();
        }
        
        // 로그인한 회원 정보를 토대로 access/refresh token 발급받기
        String accessToken = jwtUtil.createAccessToken(loginUser.getId());
        String refreshToken = jwtUtil.createRefreshToken(loginUser.getId());
        
        memberService.saveRefreshToken(loginUser.getId(), refreshToken); //	발급받은 refresh token 을 DB에 저장
        
        Map<String, Object> map = new HashMap<>();
        map.put("access-token", accessToken);
        map.put("refresh-token", refreshToken);
        
        CommonResponse<?> response = CommonResponse.ok(map);
        
        ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(200).body(response);
        
		return responseEntity;
    }

    @Operation(summary = "로그아웃", description = "회원 정보를 담은 Token 을 제거한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "memberid", description = "회원 ID")
    @GetMapping("/logout/{memberid}")
    public ResponseEntity<CommonResponse<?>> logout(@PathVariable("memberid") @NotBlank String memberId) throws Exception {
        memberService.deleRefreshToken(memberId);
        
        CommonResponse<?> response = CommonResponse.ok();
        
        ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(200).body(response);
        
        return responseEntity;
    }

    @Hidden
    @Operation(summary = "회원 인증", description = "access token으로 유효한 회원인지 인증한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 인증 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "memberid", description = "회원 ID")
    @GetMapping("/info/{memberid}")
    public ResponseEntity<CommonResponse<?>> getUserInfo(@PathVariable("memberid") @NotNull String memberId) throws Exception {
        MemberDto member = memberService.getUserInfo(memberId);
        
        Map<String, Object> map = new HashMap<>();
        map.put("user", member);
        
        CommonResponse<?> response = CommonResponse.ok(map);
        
        ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(200).body(response);
        
        return responseEntity;
    }

    @Hidden
    @Operation(summary = "Access Token 재발급", description = "refresh token으로 만료된 access token 을 재발급 받는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "memberid", description = "회원 ID")
    @PostMapping("/refresh/{memberid}")
    public ResponseEntity<CommonResponse<?>> reissueAccessToken(@PathVariable("memberid") @NotBlank String memberId, HttpServletRequest request) throws Exception {
        String headerRefreshToken = request.getHeader("refreshToken");
        log.debug("refreshToken: " + headerRefreshToken);
        if (!jwtUtil.checkToken(headerRefreshToken) || (headerRefreshToken.equals(memberService.getRefreshToken(memberId)))) {
        	CommonResponse<?> response = CommonResponse.failure(RetConsts.ERR401, "해당 토큰은 사용 불가능합니다.");
        	
        	ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(401).body(response);
        	
        	return responseEntity;
        }
        String accessToken = jwtUtil.createAccessToken(memberId);

        Map<String, Object> map = new HashMap<>();
        map.put("access-token", accessToken);
        
        CommonResponse<?> response = CommonResponse.ok(map);
        
        ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(200).body(response);
        
        return responseEntity;
    }
    
    @Operation(summary = "회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
			@ApiResponse(responseCode = "400", description = "입력값 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
			@ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Parameter(name = "memberid", description = "회원 ID")
    @DeleteMapping("/{memberid}")
    public ResponseEntity<CommonResponse<?>> deleteUser(@PathVariable("memberid") @NotBlank String memberId) throws Exception {
    	memberService.deleteUser(memberId);
    	
    	CommonResponse<?> response = CommonResponse.ok();
        
        ResponseEntity<CommonResponse<?>> responseEntity = ResponseEntity.status(200).body(response);
        
        return responseEntity;
    }

}