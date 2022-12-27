package com.example.apigatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@Component
public class AthorizationHeaderFilter extends AbstractGatewayFilterFactory<AthorizationHeaderFilter.Config> {
	private static final Logger logger = LoggerFactory.getLogger(AthorizationHeaderFilter.class);
	
	private Environment env;
    String id;
    

    public AthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    @Override
    public GatewayFilter apply(Config config) {
        //Custom Pre Filter
        logger.info("[apigateway-service]apply()");
        logger.info("====== token.secret :" + env.getProperty("token.secret") + "=======");
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            org.springframework.http.HttpHeaders  headers=  request.getHeaders();
            logger.info("headers="+headers.containsKey("cookie"));//headers=true
            
            logger.info("remote Address = " + request.getRemoteAddress());
            logger.info("요청 주소 = " + request.getURI());
            
            //헤더 안에 authorization가 있으면
            if(headers.containsKey("authorization")) {
            	//Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY2NjI0MzQ1OH0.JQVTf8OHkOawZzd6IPAVXgT74N1XW9Zrst049T1q2aw
                
            	logger.info("user_token="+headers.get("authorization"));
            	String autorizationHeader = headers.get("authorization").get(0);
	                    //토큰의 구성은 "Bearer 발행한토큰" 형태로 나타납니다. 그래서 발행한 토큰만 가져오기 위해 Bearer를 제거합니다.
	                    //Bearer 토큰은 토큰을 소유한 사람에게 액세스 권한을 부여하는 일반적인 토큰 클래스 입니다.  
	                        String jwt = autorizationHeader.replace("Bearer ", "");

	                        if (!isJwtValid(jwt, request)) {  //헤더에서 발행한 토큰을 인자값으로 토큰이 유효한 것인지를 판별하는 메서드를 실행합니다.
	                        	logger.info("토큰 유효하지 않음");
	                        	return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
	                        }else {
	                        	logger.info("토큰 인증 완료");
	                        }
	                        
            }else {
            	//헤더 안에 authorization가 없으면 에러 메시지 출력합니다.
            	return onError(exchange, "No autorization header", HttpStatus.UNAUTHORIZED);
            }
            

            //Custom Post Filter
            return chain.filter(exchange);
        });
    }


    //Mono(반환값 한 개), Flux(반환값 여러개) -> Spring WebFlux
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        logger.info("[apigateway-service]onError()");
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        logger.info(err);
        return response.setComplete();

    }
    
    public String getSubject(String token,ServerHttpRequest request) {
    	String key=env.getProperty("token.secret");//accessToken
    	
    	 Claims claims=null;
 		try {
 			claims = Jwts.parser()
 			        //.setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
 					.setSigningKey(key.getBytes("UTF-8"))
 			        .parseClaimsJws(token).getBody();
 			return claims.getSubject();
 		} catch (ExpiredJwtException e) { //JWT를 생성할 때 지정한 유효기간 초과할 때. 
 			
 			return "expired!";
 		}catch(Exception ex) {
 			return "";
 		}
    }

    private boolean isJwtValid(String jwt,ServerHttpRequest request) {
    	 
    	if(getSubject(jwt, request).isEmpty()) {
    		return false;
    	}

    	return true;
}
    
    private byte[] generateKey(){
    	String key=env.getProperty("token.secret");
		byte[] generateKey = null;
		try {
			generateKey = key.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
				logger.error("Making JWT Key Error ::: {}", e.getMessage());
		}
		
		return generateKey;
	}

    
    public static class Config{

    }
    
    
    private String createToken(String subject, long time) {
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
        return token;//직렬화, 문자열로 변경
	}
   


    
}
