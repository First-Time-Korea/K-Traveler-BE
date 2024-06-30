package com.ssafy.firskorea.common.exception;

public class DuplicationMemberIdException extends RuntimeException {

	public DuplicationMemberIdException() {
		super("중복된 아이디가 존재합니다.");
	}

}
