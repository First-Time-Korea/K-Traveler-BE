package com.ssafy.firskorea.common.exception;

public class MemberAlreadyWithdrawnException extends RuntimeException {

	public MemberAlreadyWithdrawnException() {
		super("이미 탈퇴한 회원입니다.");
	}

}
