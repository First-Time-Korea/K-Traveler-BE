package com.ssafy.firskorea.intercepter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ssafy.firskorea.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
	
	@Autowired
	private JWTUtil jwtUtil; 

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 회원 인증 제외
		String servletPath = request.getServletPath();
		String board = "/articles";
		if (servletPath.startsWith(board)) {
			String type = servletPath.substring(board.length());
			String method = request.getMethod();

			if (type.equals("") && "GET".equalsIgnoreCase(method)) {  // 여행 후기 리스트 조회(/articles)
				return true;
			} else if (type.matches("/\\d+$") && "GET".equalsIgnoreCase(method)) {  // 여행 후기 조회(/articles/{articleid})
				return true;
			}
		}
		
		String accessToken = request.getHeader("Authorization");

		if (accessToken == null || !jwtUtil.checkToken(accessToken)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		
		return true;
	}

}