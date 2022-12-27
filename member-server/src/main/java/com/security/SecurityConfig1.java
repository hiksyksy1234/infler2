package com.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jwt.JwtRequestFilter;
import com.naver.security.CustomAccessDeniedHandler;
import com.naver.security.CustomUserDetailsService;


//https://onethejay.tistory.com/130?category=1058743
@EnableWebSecurity // 스프링과 시큐리티 결합
@Configuration
public class SecurityConfig1 extends WebSecurityConfigurerAdapter {
	
	private JwtRequestFilter jwtRequestFilter;
	
	@Autowired
	public SecurityConfig1(JwtRequestFilter jwtRequestFilter) {
		this.jwtRequestFilter = jwtRequestFilter;
	}

	// <security:http> 설정 부분
		@Override
		//권한 관련 설정을 합니다.
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();
			http.authorizeRequests()
			.antMatchers("/resources/**/**").permitAll()
			.antMatchers("/actuator/**/**").permitAll()
			.antMatchers(HttpMethod.GET,"/members/idcheck").permitAll()
			//.antMatchers(HttpMethod.GET,"/members/**").access("hasRole('ROLE_MEMBER')")
				.antMatchers("/**").permitAll();
					//.and()
					//.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
			        
			http.formLogin().disable();
			http.cors();
			//http.csrf().disable();
			//https://zzang9ha.tistory.com/341
			// csrf정보를 쿠키정보로 받을 수 있습니다. 
			// 브라우저를 종료하면 쿠키도 사라집니다.
			// 쿠키 유효시간을 설정하는 빈을 주입하도록 합니다.
			
			// CsrfTokenRepository는 "XSRF-TOKEN"이라는 이름의 쿠키에서 CSRF 토큰을 가지고 있습니다. 
			/*
			 * Http Only Cookie는 클라이언트측 스크립트가 데이터에 접근하는 것을 방지하는 브라우저 쿠키에 추가된 태그입니다.
               이는 서버 이외의 다른 사용자가 특수 쿠키에 접근하지 못하도록 하는 게이트를 제공합니다.
               쿠키를 생성할 때 HttpOnly 태그를 사용하면 클라이언트 스크립트가 보호된 쿠키에 액세스하는 위험을 줄일 수 있으므로 
               쿠키의 보안을 강화할 수 있습니다.
			 */
			//http.csrf().csrfTokenRepository( getCTR());
			
			
			//http.formLogin().loginPage("/login").and().addFilter(null);
//			http.csrf().disable().authorizeRequests().anyRequest().permitAll()
//			.and()
//			//.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//			//.and()
//			//.formLogin()
//			//.disable()
//			.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
			
			http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
		}




		//로그인 처리를 위한 곳 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserService()).passwordEncoder(passwordEncoder());
	}


	/*
	 * 1. UserDetailsService 인터페이스는 DB에서 유저 정보를 불러오는 loadUserByUsername()가 존재합니다. 이를
	 * 구현하는 클래스는 DB에서 유저의 정보를 가져와서 UserDetails 타입으로 리턴해 주는 작업을 합니다. 2. UserDetails는
	 * 인터페이스로 Security에서 사용자의 정보를 담는 인터페이스입니다. 3. UserDetails 인터페이스를 구현한 클래스는 실제로
	 * 사용자의 정보와 사용자가가 가진 권한의 정보를 처리해서 반환하게 됩니다. 예) UserDetails user = new
	 * User(username, users.getPassword(), roles);
	 */
	@Bean
	public UserDetailsService customUserService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	
	
	/**
     * AuthenticationManager Bean 등록
     * Custom Login에서 사용
     * UsernamePasswordAuthenticationToken을 파라미터로 인증을 수행
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
