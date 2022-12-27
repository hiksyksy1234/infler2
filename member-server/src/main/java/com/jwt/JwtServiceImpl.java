package com.jwt;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hta.vue.HomeController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
/*
 * 
 * https://alwayspr.tistory.com/8
 * https://github.com/viviennes7/luvook
 */
@Service
public class JwtServiceImpl implements JwtService{
	private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);
	private Environment env;    
    
    @Autowired
    public JwtServiceImpl(Environment env) {
    	this.env=env;
   }

    // 토큰 발행
    @Override
    public String createToken(String subject, long time) {
        if (time <= 0) {
            throw new RuntimeException("Expiry time must be greater than Zero : ["+time+"] ");
        }
        // 토큰을 서명하기 위해 사용해야할 알고리즘 선택
        SignatureAlgorithm  signatureAlgorithm = SignatureAlgorithm.HS256;

        logger.info("1");
       // byte[] secretKeyBytes =	DatatypeConverter.parseBase64Binary(SECRET_KEY);
        logger.info("2");
       // Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)  //먼저 Claims 인스턴스가 JWT 본문으로 존재하는지 확인한 다음 지정된 값으로 Claims 제목 필드를 설정합니다
                .signWith(signatureAlgorithm, this.generateKey());//지정된 키로 지정된 알고리즘을 사용하여 구서된 JWT에 서명하여 JWS를 생성합니다.
       
        long nowTime = System.currentTimeMillis();
        builder.setExpiration(new Date(nowTime + time));//현재 사간 + time을 토큰 유효시간으로 설정합니다.
        String token = builder.compact();
        HomeController.token=token;
        return token;//직렬화, 문자열로 변경
	}

    public String getSecretKey() {
    	return env.getProperty("token.secret");
    }
    
    private byte[] generateKey(){
    	String secretKey=env.getProperty("token.secret");//accessToken
		logger.info("secretKey="+secretKey);
    	byte[] key = null;
		try {
			key = secretKey.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
				logger.error("Making JWT Key Error ::: {}", e.getMessage());
		}
		
		return key;
	}

	// 토큰 해독
    @Override
    public String getSubject(String token) {
    	String secretKey=env.getProperty("token.secret");//accessToken
        Claims claims=null;
		try {
			claims = Jwts.parser()
			        //.setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
					.setSigningKey(secretKey.getBytes("UTF-8"))
			        .parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) { //JWT를 생성할 때 지정한 유효기간 초과할 때.
			logger.info("토큰 만료 오류 발생 다시 토큰 발행");
			
			String id = decode(token);//id={"sub":"admin","exp":1664782463}
			logger.info("id="+id);
			
			String refreshToken=createToken(HomeController.id, (1000*60*100));
			logger.info("refreshToken="+refreshToken);
			try {
			claims = Jwts.parser()
					.setSigningKey(secretKey.getBytes("UTF-8"))
			        .parseClaimsJws(refreshToken).getBody();
			}catch(Exception e1) {
				e.printStackTrace();
			}
		} catch (UnsupportedJwtException e) { //예상하는 형식과 일치하지 않는 특정 형식이나 구성의 JWT일 때
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedJwtException e) { //JWT가 올바르게 구성되지 않았을 때
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) { //JWT의 기존 서명을 확인하지 못했을 때
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return claims.getSubject();
    }

    @Override
    public boolean isUsable(String token) {
    	logger.info("isUsable");
        try{
            Claims claims = Jwts.parser()
                  //  .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
            		.setSigningKey(this.generateKey())
                    .parseClaimsJws(token).getBody();
            return true;

        }catch (Exception e) {
            //throw new Exception("접속 권한이 없습니다.");
        	return false;
        }
    }
    
public String decode(String token) {
		
		String[] splitToken = token.split("\\.");
		Decoder decoder = Base64.getDecoder();
		byte[] decodedBytes = decoder.decode(splitToken[1]);
		
		String decodedString = null;
		try {
			decodedString = new String(decodedBytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//decodedString={"sub":"admin","exp":1664782463}
		String sub_word = decodedString.split(",")[0].substring(7);
		return sub_word;
		//return decodedString;
		
	}



}