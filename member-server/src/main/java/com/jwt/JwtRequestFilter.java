package com.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.naver.myhome4.domain.Member;
import com.naver.myhome4.mybatis.mapper.MemberMapper;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
	
	private  JwtService jwtservice;
	
	private MemberMapper dao;
	
	public JwtRequestFilter(JwtService jwtservice, MemberMapper dao) {
		this.jwtservice=jwtservice;
		this.dao = dao;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {
		
		logger.info("====== 여기는 필터-start =====");
		// TODO Auto-generated method stub
		
		logger.info("========token.secret=============");
		logger.info(jwtservice.getSecretKey());
		
		 List<String> excludedUrls = new ArrayList<>();
		 excludedUrls.add("/members/idcheck");
		 excludedUrls.add("/members/logout");
		 excludedUrls.add("/members/new");
		 excludedUrls.add("/members");
		 excludedUrls.add("/login");
		 excludedUrls.add("/user/login");
		 excludedUrls.add("/join");
		 excludedUrls.add("/getSession");
		 excludedUrls.add("/health_check");
		 excludedUrls.add("/actuator/health");
		 excludedUrls.add("/actuator/info");
		 excludedUrls.add("/actuator/refresh");
		 excludedUrls.add("/actuator/beans");
		 excludedUrls.add("/actuator/busrefresh");
		 excludedUrls.add("/");
		 logger.info("요청 URL=" + request.getRequestURL());
		 logger.info("요청 URI=" + request.getRequestURI());
		 logger.info("요청 POST = " + request.getMethod());
		logger.info(request.getRequestURI().substring(request.getContextPath().length()));
		 try {
			 
//			    //해당 경로로 요청하는 경우 필터를 통과합니다.
//	            if ("/members/idcheck".equals(request.getRequestURI().substring(request.getContextPath().length()))) {
//	            	logger.info(request.getRequestURI());
//	            	logger.info("여기는 jwt 필터");
//	                doFilter(request, response, filterChain);
//	            }else if ("/join".equals(request.getRequestURI())) {
//	                doFilter(request, response, filterChain);
//	            }else if ("/idcheck".equals(request.getRequestURI())) {
//	                doFilter(request, response, filterChain);
//	            	
//	            }
			 //request.getRequestURI() : /vue/members/idcheck  => /members/idcheck로 추출
			 // list에 담긴 모든 값들에 대해  확인
			    String path =request.getRequestURI().substring(request.getContextPath().length());
			    logger.info("필터통과전:"+path);
			    if(excludedUrls.contains(path) || path.startsWith("/members/")) {//  /members/{id} 부분도 통과 할 수 있도록 합니다.
			    	logger.info("필터통과1");
			    	doFilter(request, response, filterChain); //필터 무시합니다.
			    }else if(path.contains("/js") || path.contains("/css") || path.contains("ico") ||  path.contains("welcome") ) {
			    	logger.info("필터통과2");
			    	doFilter(request, response, filterChain); //필터 무시합니다.
			    }
	            else {
	                String token = parseJwt(request);
	                if (token == null) {
	                    response.sendError(403);    //accessDenied
	                } else {
	                	//토큰 생성시 subject로 넣었던 아이디 값을 가져옵니다.
	                    String tokenInfo = jwtservice.getSubject(token);
	                    if (tokenInfo != null) {
	                       // UserDetails loginUser = userService.loadUserByUsername(userId);
	                        Member member = dao.isId(tokenInfo);
	                        
	                      //GrantedAuthority : 인증 개체에 부여된 권한을 나타내기 위한 인터페이스로 이를 구현한 구현체는  생성자에 권한을 문자열로 넣어주면 됩니다.
	                        //SimpleGrantedAuthority : GrantedAuthority의  구현체입니다.
	                          Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();

	                 	     roles.add(new SimpleGrantedAuthority(member.getAuth()));
	                 	     
	                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
	                                member.getId(), null, roles
	                        );

	                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                        SecurityContextHolder.getContext().setAuthentication(authentication);
	                        
	                        logger.info("토큰 인증 완료");
	                        
	                        doFilter(request, response, filterChain);

	                    } else {
	                        logger.error("### TokenInfo is Null");
	                    }
	                }
	            }
	        } catch (Exception e) {
	            logger.error("### Filter Exception {}", e.getMessage());
	        }
	    }

	
	    private String parseJwt(HttpServletRequest request) {
	    	//요청 헤더에서 "Authorization"에 해당하는 헤더 값을 가져옵니다.
	        String headerValue = request.getHeader("authorization");
	        
	        //StringUtils.hasText(args) : args가 null이 아니고 문자열의 길이가 0보다 크고 공백이 아닌 경우 true를 리턴
	        //headerValue.startsWith("Bearer ") : headerValue가 "Bearer "로 시작하는 경우 true를 리턴
	        if (StringUtils.hasText(headerValue) && headerValue.startsWith("Bearer ")) {
	            return headerValue.substring(7, headerValue.length());
	        }
	        return null;
	    }
	}
