package com.ssafy.firskorea.common.exception;

public class IncorrectMemberException extends RuntimeException {

	public IncorrectMemberException() {
		super("아이디 또는 패스워드를 확인해주세요.");
	}

}
