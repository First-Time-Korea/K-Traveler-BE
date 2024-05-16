package com.ssafy.firskorea.member.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.firskorea.member.dto.MemberDto;
import com.ssafy.firskorea.member.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {

	private MemberMapper memberMapper;

	@Autowired
	public MemberServiceImpl(MemberMapper memberMapper) {
		super();
		this.memberMapper = memberMapper;
	}

	@Override
	public MemberDto login(MemberDto memberDto) throws Exception {
		System.out.println(memberMapper.login(memberDto));
		return memberMapper.login(memberDto);
	}

	@Override
	public MemberDto userInfo(String id) throws Exception {
		return memberMapper.userInfo(id);
	}

	@Override
	public void saveRefreshToken(String id, String refreshToken) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("token", refreshToken);
		memberMapper.saveRefreshToken(map);
	}

	@Override
	public Object getRefreshToken(String id) throws Exception {
		return memberMapper.getRefreshToken(id);
	}

	@Override
	public void deleRefreshToken(String id) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("token", null);
		memberMapper.deleteRefreshToken(map);
	}

	@Override
	public void signUp(MemberDto memberDto) throws Exception {
		memberMapper.signUp(memberDto);
	}

	@Override
	public boolean idCheck(String id) throws Exception {
		if(memberMapper.idCheck(id)>=1){
			return false;
		}
		return true;
	}
}
