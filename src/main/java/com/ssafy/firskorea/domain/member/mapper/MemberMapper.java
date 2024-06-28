package com.ssafy.firskorea.domain.member.mapper;

import java.sql.SQLException;
import java.util.Map;

import com.ssafy.firskorea.domain.member.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
	MemberDto login(MemberDto memberDto) throws SQLException;

	MemberDto userInfo(String id) throws SQLException;

	void saveRefreshToken(Map<String, String> map) throws SQLException;

	Object getRefreshToken(String id) throws SQLException;

	void deleteRefreshToken(Map<String, String> map) throws SQLException;

	void signUp(MemberDto memberDto)  throws SQLException;

	int idCheck(String id) throws SQLException;
}
