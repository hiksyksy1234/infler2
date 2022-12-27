package com.hta.vue;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * CORS란? CORS(Cross-origin resource sharing)이란, 
 * 웹 페이지의 제한된 자원을 외부 도메인에서 접근을
 * 허용해주는 메커니즘입니다.
 */
//@CrossOrigin(origins = "http://localhost:8081")
@Controller
public class HomeController {
	public static String token="";
	public static String id="";
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	Environment env;
/*
 * https://codevang.tistory.com/246
 "/"       :    "IP:포트/context/" URL 매핑
"/*"     :    "IP:포트/context/아무거나~" URL 매핑
"/**"   :    "IP:포트/context/아무거나/아무거나/아무거나~" 모든 URL 매핑
 */
	
	/*https://thalals.tistory.com/268
	 * ResponseEntity란, httpentity를 상속받는, 결과 데이터와 HTTP 상태 코드를 직접 제어할 수 있는 클래스입니다.
	 * ResponseEntity에는 사용자의  HttpRequest에 대한 응답 데이터가 포함됩니다.
	 */
	
	
	@RequestMapping(value = "/members/getId", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getSession(Principal principal) {
		String username="";
		if(principal != null) {
			 username = principal.getName();
		}
		logger.info("[getSession]id=" + username);	
		id=username;
		HttpHeaders header = new HttpHeaders();
		logger.info(token);
		header.add("authorization", token);
		return  new ResponseEntity<String>(username, header,HttpStatus.OK);
	}
	
	@GetMapping("/health_check")
	@ResponseBody
	public String status(){
	     return String.format("It's Working in User Service"
	             + ", port(local.server.port)=" + env.getProperty("local.server.port")
	             + ", port(server.port)=" + env.getProperty("server.port")
	             + ", token.secret=" + env.getProperty("token.secret")
	             + ", token.expiration_time=" + env.getProperty("token.expiration_time"));
	}

		
		
	
		
		
			
}









