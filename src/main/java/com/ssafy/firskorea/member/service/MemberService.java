package com.ssafy.firskorea.member.service;

import com.ssafy.firskorea.member.dto.MemberDto;

public interface MemberService {
	MemberDto login(MemberDto memberDto) throws Exception;

	public MemberDto userInfo(String userId) throws Exception;

	void saveRefreshToken(String id, String refreshToken) throws Exception;

	Object getRefreshToken(String id) throws Exception;

	void deleRefreshToken(String id) throws Exception;

	void signUp(MemberDto memberDto) throws Exception;
}
