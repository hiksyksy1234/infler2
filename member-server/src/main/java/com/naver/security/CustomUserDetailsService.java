package com.naver.security;


import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;

/*
출처: https://to-dy.tistory.com/86 [todyDev]
DB에서 유저 정보를 직접 가져오는 인터페이스를 구현해 봅시다.
 UserDetailsService 인터페이스에는 DB에서 유저 정보를 불러오는 중요한 메소드가 존재합니다.
  바로 loadUserByUsername() 메소드입니다. 이 메소드에서 유저 정보를 불러오는 작업을 하면 됩니다.
   UserDetailsService 인터페이스를 구현하면 loadUserByUsername() 메소드가 오버라이드 될 것이다. 
    여기에서 CustomUserDetails 형으로 사용자의 정보를 가져오면 된다. 
    가져온 사용자의 정보를 유/무에 따라 예외와 사용자 정보를 리턴하면 된다. 
    다시 한번 말하자면 이 부분은 DB에서 유저의 정보를 가져와서 리턴해주는 작업이다. 
 */
import com.naver.myhome4.domain.Member;
import com.naver.myhome4.mybatis.mapper.MemberMapper;

public class CustomUserDetailsService implements UserDetailsService {
	private static final Logger logger 
    = LoggerFactory.getLogger(CustomUserDetailsService.class);	
	
	@Autowired
	private MemberMapper dao;


	@Override
    public UserDetails loadUserByUsername(@RequestBody String username) throws UsernameNotFoundException {
		logger.info("username은 로그인시 입력한 값 : " + username );
    	Member users = dao.isId(username);
       if (users==null) {
    	   logger.info("username " + username + " not found");
    	   
    	  throw new UsernameNotFoundException("username " + username + " not found");
    	  
       }
       
       //GrantedAuthority : 인증 개체에 부여된 권한을 나타내기 위한 인터페이스로 이를 구현한 구현체는  생성자에 권한을 문자열로 넣어주면 됩니다.
       //SimpleGrantedAuthority : GrantedAuthority의  구현체입니다.
         Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();

	     roles.add(new SimpleGrantedAuthority(users.getAuth()));
         if(username.equals("admin")) {
        	 roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
         }
	    UserDetails user = new User(username, users.getPassword(), roles);
       return user;
    }
}