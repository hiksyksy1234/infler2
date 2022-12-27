package com.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

//@Component
public class JwtInterceptor implements HandlerInterceptor {
    private static final String HEADER_AUTH = "authorization";

    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);
    
    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String token = request.getHeader(HEADER_AUTH);
        logger.info(token);
        
        if(request.getMethod().equals("OPTIONS")) { // preflight로 넘어온 options는 통과
			return true;
		}
        
        if(token != null && jwtService.isUsable(token)){
            return true;
        }else{// 유효한 인증토큰이 아닐 경우
            //throw new UnauthorizedException();
        	return false;
        }
    }
}
