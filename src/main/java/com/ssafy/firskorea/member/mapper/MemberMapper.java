package com.ssafy.firskorea.member.mapper;

import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.firskorea.member.dto.MemberDto;

@Mapper
public interface MemberMapper {
	MemberDto login(MemberDto memberDto) throws SQLException;

	MemberDto userInfo(String id) throws SQLException;

	void saveRefreshToken(Map<String, String> map) throws SQLException;

	Object getRefreshToken(String id) throws SQLException;

	void deleteRefreshToken(Map<String, String> map) throws SQLException;

	void signUp(MemberDto memberDto);
}
