package com.ssafy.firskorea.common.exception;

public class InvalidRefreshTokenException extends RuntimeException {

	public InvalidRefreshTokenException() {
		super("해당 토큰은 사용 불가능합니다.");
	}

}
