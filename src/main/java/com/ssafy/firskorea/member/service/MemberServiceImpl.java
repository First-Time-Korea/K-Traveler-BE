package com.ssafy.firskorea.member.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.firskorea.member.dto.MemberDto;
import com.ssafy.firskorea.member.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {

	private MemberMapper memberMapper;

	public MemberServiceImpl(MemberMapper memberMapper) {
		super();
		this.memberMapper = memberMapper;
	}

	/**
	 * 회원 ID, 이름, 비밀번호, 이메일 아이디/도메인을 토대로 회원을 생성한다.
	 * 
	 * @param {@link MemberDto} 회원가입에 필요한 정보를 담은 전송 객체다.
	 */
	@Override
	public void signUp(MemberDto member) throws Exception {
		memberMapper.signUp(member);
	}

	/**
	 * 회원 ID 중복성을 체크한다.
	 * 
	 * @param userId 회원 ID이다.
	 * @boolean 회원 ID 중복성 여부로, true면 중복되지 않은 것이고 false면 중복된 것이다.
	 */
	@Override
	public boolean checkDuplicationUserId(String userId) throws Exception {
		if (memberMapper.checkDuplicationUserId(userId) >= 1) {
			return false;
		}

		return true;
	}

	/**
	 * 회원 ID와 비밀번호를 토대로 로그인한다.
	 * 
	 * @param {@link MemberDto} 로그인에 필요한 정보를 담은 전송 객체다.
	 * @return 로그인을 성공한 회원 정보가 담긴 {@link MemberDto} 객체를 반환한다.
	 */
	@Override
	public MemberDto login(MemberDto member) throws Exception {
		return memberMapper.login(member);
	}

	/**
	 * 회원 ID에 해당하는 회원 정보에 JWT refresh token을 저장한다.
	 * 
	 * @param userId       회원 ID이다.
	 * @param refreshToken JWT refresh token이다.
	 */
	@Override
	public void saveRefreshToken(String userId, String refreshToken) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);
		map.put("token", refreshToken);

		memberMapper.saveRefreshToken(map);
	}

	/**
	 * 회원 ID에 대응하는 JWT refresh token을 삭제한다.
	 * 
	 * @param userId 회원 ID이다.
	 */
	@Override
	public void deleRefreshToken(String userId) throws Exception {
		memberMapper.deleteRefreshToken(userId);
	}

	/**
	 * 회원 ID에 대응하는 회원 정보를 조회한다.
	 * 
	 * @param userId 회원 ID이다.
	 * @return 조회한 회원 정보가 담긴 {@link MemberDto} 객체를 반환한다.
	 */
	@Override
	public MemberDto getUserInfo(String userId) throws Exception {
		return memberMapper.getUserInfo(userId);
	}

	/**
	 * 회원 ID에 대응하는 refresh token을 조회한다.
	 * 
	 * @param userId 회원 ID이다.
	 * @return 조회한 refresh token이다.
	 */
	@Override
	public String getRefreshToken(String userId) throws Exception {
		return memberMapper.getRefreshToken(userId);
	}

	/**
	 * 회원 ID에 대응하는 회원을 삭제한다.
	 * 
	 * @param userId 회원 ID이다.
	 */
	@Override
	public void deleteUser(String userId) throws Exception {
		memberMapper.deleteUser(userId);
	}

}
