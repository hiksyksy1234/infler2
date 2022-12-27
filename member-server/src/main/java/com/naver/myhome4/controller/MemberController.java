package com.naver.myhome4.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.JwtService;
import com.naver.myhome4.domain.Member;
import com.naver.myhome4.service.MemberService;

/*
 @Component를 이용해서 스프링 컨테이너가 해당 클래스 객체를 생성하도록 설정할 수 있지만   
  모든 클래스에 @Component를 할당하면 어떤 클래스가 어떤 역할을 수행하는지 파악하기 
  어렵습니다. 스프링 프레임워크에서는 이런 클래스들을 분류하기 위해서
 @Component를 상속하여 다음과 같은 세 개의 애노테이션을 제공합니다.
 
 1. @Controller - 사용자의 요청을 제어하는 Controller 클래스
 2. @Repository - 데이터 베이스 연동을 처리하는 DAO클래스
 3. @Service - 비즈니스 로직을 처리하는 Service 클래스 

*/
/**
 * CORS(Cross-origin resource sharing) 
  1.  웹 페이지의 제한된 자원을 외부 도메인에서 접근을 허용해주는 메커니즘입니다.
  
  2.  origin이란 URL 구조에서 protocol, host, port를 합친 것을 의미합니다.
  
  3.  예) http://localhost:8088/vue/login
         프로토콜://host:포트번호/경로
         
         http://localhost:8081/경로 에서 http://localhost:8088/경로를 호출하면 origin이 다르게 때문에 
         브라우저에서는 동일 출처 정책(Same Origin Policy)에 의해 오류가 발생합니다.
         
  4. 위 문제를 해결하기 위한 설정 방법       
    (1)  각 컨트롤러에서 설정
     @CrossOrigin(origins = "http://localhost:8081")
  
    (2) 환경설정 파일에 설정
       예) WebMvcConfig.java에서 
 
     @Override
	   public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        .allowedOrigins("http://localhost:8081");
	}
 */
//@CrossOrigin(origins = "http://localhost:8081")

@RestController
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	private MemberService memberservice;
	private JwtService jwtService;
	
	@Autowired
	public MemberController(MemberService memberservice,JwtService jwtService ) {
		this.memberservice = memberservice;
		this.jwtService = jwtService;
	}

	@GetMapping("/welcome")
	public String welcome() {
		return "welcome-member-service";
	}
	
	
	
	
	// 로그아웃
	@GetMapping(value = "/members/logout")
	public ResponseEntity<String> loginout(HttpSession session) {
		logger.info("logout");
		session.invalidate();
		return new ResponseEntity<>("logout success", HttpStatus.OK);
	}
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	//vue.js @RequestBody Member member,  => Member member
	// 로그인 처리
	@PostMapping(value = "/members")
	public ResponseEntity<Integer>  loginProcess(
		Member member,
		HttpServletResponse response, 
		HttpSession session) {
		 int result=0;
			String username = member.getId();
			String password = member.getPassword();

			// username과 password를 이용해서 UsernamePasswordAuthenticationToken을 생성합니다.
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
	        logger.info(token.toString());
			logger.info("1");
			
			// SecurityConfig에서 Bean으로 등록한 AuthenticationManager를 이용해서 인증을 수행합니다.
			/*
			 * @Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(customUserService()).passwordEncoder(passwordEncoder());
		}
			 */
			try {
				logger.info("1-1");
				Authentication authentication = authenticationManager.authenticate(token);
				result=1;//윗 문장에서 아이디와 비번이 옳으면 이곳으로 이동하고 오류가 발생하면 이곳으로 오지 않습니다.
				logger.info(authentication.toString());
				logger.info("2");
				// SecurityContextHolder → getContext 메서드를 통해 context를 가져온 후, 인증 정보를 set →
				// SecurityContext에 인증 값 설정됨
				SecurityContextHolder.getContext().setAuthentication(authentication);
				logger.info("3");
				// session에 SecurityContext 값을 넣음
				session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
						SecurityContextHolder.getContext());
				logger.info("로그인 성공");
				
				String jwttoken = jwtService.createToken(member.getId(), ( 1000 * 60 *1));
				logger.info("jwttokin="+jwttoken);
	            response.setHeader("authorization", jwttoken);
				
			}catch(Exception e) {
				if(e instanceof UsernameNotFoundException) {
					logger.info("UsernameNotFoundException");
				}
				logger.info("134=" + e.getMessage());
			    e.printStackTrace();
				result=-1;
			}
			/*
			 * Admin admin = adminRepository.findByAdminId(adminId).orElseThrow( () -> new
			 * IllegalArgumentException("아이디 혹은 비밀번호를 확인해주세요."));
			 * admin.setTokenId(RequestContextHolder.currentRequestAttributes().getSessionId
			 * ());
			 */

			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	

	// 회원가입폼에서 아이디 검사
	@GetMapping(value = "/members/idcheck")
	public ResponseEntity<Integer>  idcheck(String id) {
		logger.info("idcheck");
		int result= memberservice.isId(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	//vue.js에서 @RequestBoay Member member => jsp에서는 Member member
	// 회원가입처리
	@PostMapping(value = "/members/new")
	public ResponseEntity<Integer> joinProcess(Member member)  {
		// 비밀번호 암호화 추가
				String encPassword = passwordEncoder.encode(member.getPassword());
				logger.info(encPassword);
				member.setPassword(encPassword);
				int result= memberservice.insert(member);
				return new ResponseEntity<>(result, HttpStatus.OK);
	}
		
	
	
	
	
	/*	new ResponseEntity 이용한 리턴
		@RequestMapping(value = "/loginProcess", method = {RequestMethod.POST, RequestMethod.OPTIONS})
		public ResponseEntity<Integer> loginProcess(
				@RequestBody Member member,
				HttpServletResponse response,
				HttpSession session) {
            String id= member.getId();
            String password=member.getPassword();
			int result = memberservice.isId(id, password);
			logger.info("결과 : " + result);
			if (result == 1) {
				// 로그인 성공
				session.setAttribute("id", id);
			}
			return new ResponseEntity<Integer>(result,HttpStatus.OK );

		}
		*/

	
	

	

	@GetMapping(value = "/members")
	public ResponseEntity<Map<String,Object>> memberList(
			@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "limit", defaultValue = "3", required = false) int limit, 
			@RequestParam(value = "search_field", defaultValue = "") String index,
			@RequestParam(value = "search_word", defaultValue = "") String search_word) {

		List<Member> list = null;
		int listcount = 0;

		list = memberservice.getSearchList(index, search_word, page, limit);
		listcount = memberservice.getSearchListCount(index, search_word); // 총 리스트 수를 받아옴

		// 총 페이지 수
		int maxpage = (listcount + limit - 1) / limit;

		// 현재 페이지에 보여줄 시작 페이지 수(1, 11, 21 등...)
		int startpage = ((page - 1) / 10) * 10 + 1;

		// 현재 페이지에 보여줄 마지막 페이지 수(10, 20, 30 등...)
		int endpage = startpage + 10 - 1;

		if (endpage > maxpage)
			endpage = maxpage;

		Map<String, Object>  map= new HashMap<String,Object>();
		map.put("page", page);
		map.put("limit", limit);
		map.put("maxpage", maxpage);
		map.put("startpage", startpage);
		map.put("endpage", endpage);
		map.put("listcount", listcount);
		map.put("memberlist", list);
		map.put("search_field", index);
		map.put("search_word", search_word);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	// 회원의 개인 정보
	@RequestMapping(value = "/members/{id}", method = RequestMethod.GET)
	public ResponseEntity<Member> member_info(@PathVariable String id) {
		logger.info("id=" + id);
		Member member= memberservice.member_info(id);
		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	// 수정처리
	@PutMapping(value = "/members")
	public ResponseEntity<Integer> updateProcess(Member member) {
		int result =  memberservice.update(member);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 삭제
	@DeleteMapping(value = "/members/{id}")
	public ResponseEntity<Integer>  member_delete(@PathVariable String id) {
		int result =  memberservice.delete(id);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}
