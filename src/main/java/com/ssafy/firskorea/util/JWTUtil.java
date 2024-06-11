package com.ssafy.firskorea.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ssafy.firskorea.common.exception.UnAuthorizedException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTUtil {

	@Value("${jwt.salt}") // application.properties 안에서 설정한 값
	private String salt;

	@Value("${jwt.access-token.expiretime}")
	private long accessTokenExpireTime;

	@Value("${jwt.refresh-token.expiretime}")
	private long refreshTokenExpireTime;

	public String createAccessToken(String userId) {
		return create(userId, "access-token", accessTokenExpireTime);
	}

//	AccessToken에 비해 유효기간을 길게 설정.
	public String createRefreshToken(String userId) {
		return create(userId, "refresh-token", refreshTokenExpireTime);
	}

//	Token 발급
//		key : Claim에 셋팅될 key 값
//		value : Claim에 셋팅 될 data 값
//		subject : payload에 sub의 value로 들어갈 subject값
//		expire : 토큰 유효기간 설정을 위한 값
//		jwt 토큰의 구성 : header + payload + signature
	private String create(String userId, String subject, long expireTime) {
//		Payload 설정 : 생성일 (IssuedAt), 유효기간 (Expiration), 
//		토큰 제목 (Subject), 데이터 (Claim) 등 정보 세팅.
		Claims claims = Jwts.claims() // Claim = payload고, 빌드 패턴을 사용해 객체를 만든다.(key-value의 쌍이다)
				.setSubject(subject) // 토큰 제목 설정 ex) access-token, refresh-token
				.setIssuedAt(new Date()) // 생성일 설정
//				만료일 설정 (유효기간)
				.setExpiration(new Date(System.currentTimeMillis() + expireTime));

//		저장할 data의 key, value
		claims.put("userId", userId);
		// refresh 토큰은 사실 이걸 만들지 않기 때문에, 조건 걸어서 걸러도 된다. 발급용이기 때문에 정보를 얻는 목적 xx

		String jwt = Jwts.builder()
//			Header 설정 : 토큰의 타입, 해쉬 알고리즘 정보 세팅.
				.setHeaderParam("typ", "JWT").setClaims(claims)
//			Signature 설정 : secret key를 활용한 암호화.
				.signWith(SignatureAlgorithm.HS256, this.generateKey()) // generate key: 설정한 salt로 해시값을 만든다.
				.compact(); // 직렬화 처리. (객체-> 바이트스트림)

		return jwt;
	}

//	Signature 설정에 들어갈 key 생성.
	private byte[] generateKey() {
		byte[] key = null;
		try {
//			charset 설정 안하면 사용자 플랫폼의 기본 인코딩 설정으로 인코딩 됨.
			key = salt.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			if (log.isInfoEnabled()) {
				e.printStackTrace();
			} else {
				log.error("Making JWT Key Error ::: {}", e.getMessage());
			}
		}
		return key;
	}

//	전달 받은 토큰이 제대로 생성된 것인지 확인 하고 문제가 있다면 UnauthorizedException 발생.
	public boolean checkToken(String token) {
		try {
//			Json Web Signature? 서버에서 인증을 근거로 인증 정보를 서버의 private key 서명 한것을 토큰화 한것
//			setSigningKey : JWS 서명 검증을 위한  secret key 세팅
//			parseClaimsJws : 파싱하여 원본 jws 만들기
			Jws<Claims> claims = Jwts.parser().setSigningKey(this.generateKey()).parseClaimsJws(token); // 받은 토큰을 파싱한다.
//			Claims 는 Map 구현체 형태
			log.debug("claims: {}", claims);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
	}

	public String getUserId(String authorization) {
		Jws<Claims> claims = null;
		try {
			claims = Jwts.parser().setSigningKey(this.generateKey()).parseClaimsJws(authorization);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new UnAuthorizedException();
		}
		Map<String, Object> value = claims.getBody();
		log.info("value : {}", value);
		return (String) value.get("userId");
	}

}
