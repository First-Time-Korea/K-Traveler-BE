package com.ssafy.firskorea.member.service;

import com.ssafy.firskorea.member.dto.MemberDto;

public interface MemberService {

	void signUp(MemberDto member) throws Exception;

	boolean checkDuplicationMemberId(String memberId) throws Exception;

	MemberDto login(MemberDto member) throws Exception;

	void saveRefreshToken(String memberId, String refreshToken) throws Exception;

	void deleRefreshToken(String memberId) throws Exception;

	public MemberDto getUserInfo(String memberId) throws Exception;

	String getRefreshToken(String memberId) throws Exception;
	
	void deleteUser(String memberId) throws Exception;

}
