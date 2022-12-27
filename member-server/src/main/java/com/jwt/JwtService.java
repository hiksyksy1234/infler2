package com.jwt;

public interface JwtService {

	// 토큰 발행
	String createToken(String subject, long time);

	// 토큰 해독
	String getSubject(String token);

	boolean isUsable(String jwt);
	
	String getSecretKey();//rabbitmq 확인 하기 위한 메서드

}