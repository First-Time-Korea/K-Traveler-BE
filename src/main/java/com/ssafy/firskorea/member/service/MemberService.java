package com.ssafy.firskorea.member.service;

import com.ssafy.firskorea.member.dto.MemberDto;

public interface MemberService {

	void signUp(MemberDto member) throws Exception;

	boolean checkDuplicationUserId(String userId) throws Exception;

	MemberDto login(MemberDto member) throws Exception;

	void saveRefreshToken(String userId, String refreshToken) throws Exception;

	void deleRefreshToken(String userId) throws Exception;

	public MemberDto getUserInfo(String userId) throws Exception;

	String getRefreshToken(String userId) throws Exception;
	
	void deleteUser(String userId) throws Exception;

}
