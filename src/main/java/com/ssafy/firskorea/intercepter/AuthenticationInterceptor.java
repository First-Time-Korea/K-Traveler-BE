package com.ssafy.firskorea.intercepter;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 세션에서 "loginedUser" 속성 확인
		Object loginedUser = request.getSession().getAttribute("loginedUser");

		// "loginedUser"가 null이 아닌 경우에만 요청 허용
		if (loginedUser != null) {
			return true;
		} else {
			// 로그인되지 않은 사용자의 요청이므로 처리할 수 없음을 클라이언트에게 알림
			response.getWriter().write("Unauthorized access!");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}
	}

}