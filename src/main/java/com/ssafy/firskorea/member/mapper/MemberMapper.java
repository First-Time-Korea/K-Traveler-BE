package com.ssafy.firskorea.member.mapper;

import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.firskorea.member.dto.MemberDto;

@Mapper
public interface MemberMapper {

	void signUp(MemberDto memberDto) throws SQLException;

	int checkDuplicationUserId(String userId) throws SQLException;

	MemberDto login(MemberDto member) throws SQLException;

	void saveRefreshToken(Map<String, String> map) throws SQLException;

	void deleteRefreshToken(Map<String, String> map) throws SQLException;

	MemberDto getUserInfo(String userId) throws SQLException;

	String getRefreshToken(String userId) throws SQLException;

}
