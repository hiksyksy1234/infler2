package com.hta.vue;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ResponseEntityController {

	private static final Logger logger = LoggerFactory.getLogger(ResponseEntityController.class);

	// ResponseEntity 타입은 개발자가 직접 결과 데이터와 HTTP상태 코드를 직접 제어할 수 있는 클래스입니다.
	// produces 옵션에 응답 헤더로 Content-Type을 지정합니다.
	/*
	 localhost 페이지를 찾을 수 없음
	 다음 웹 주소(http://localhost:8088/vue/entity)에 대해 발견된 웹페이지가 없습니다.
     HTTP ERROR 404
	 */
	@RequestMapping(value = "/entity", method = RequestMethod.GET, 
			produces = "text/html; charset=utf8") //response.setContentType()설정하는 부분
	public ResponseEntity<String> test() {
		logger.info("entity");
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}
	
	
	
	/*
	 * 
	 페이지가 작동하지 않습니다.문제가 계속되면 사이트 소유자에게 문의하세요.
     HTTP ERROR 400
	 */
	@RequestMapping(value = "/entityerror", method = RequestMethod.GET, produces = "text/html; charset=utf8")
	public ResponseEntity<String> testerror() {
		logger.info("entityerror");
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	

	
	
	/*
	 Request URL: http://localhost:8088/myhome4/entity1
    Request Method: GET
    Status Code: 200 

    Response Header
    Content-Length: 9
    Content-Type: text/html;charset=utf8

	 */
	@RequestMapping(value = "/entity1", method = RequestMethod.GET, 
			        produces = "text/html; charset=utf8")
	public ResponseEntity<String> test1() {
		logger.info("entity1");
		String name = "홍길동";
		return new ResponseEntity<>(name, HttpStatus.OK);
	}

	
	
	
	
	
	
	
	/*
	 Request URL: http://localhost:9001/myhome3/entity2
     Request Method: GET
     Status Code: 200 

      Response Header
      Content-Length: 31
      Content-Type: application/json;charset=utf8
	 */
	/* 브라우저 출력결과
	 {"name": "홍길동","age": 21}
	 */
	//@ResponseBody
	@RequestMapping(value = "entity2", produces = "application/json; charset=utf8")
	public ResponseEntity<String>  testAjax() {

		// Json 결과값 : {"name": "홍길동","age": 21}
		String jsonResult = "{\"name\": \"홍길동\",\"age\": 21}";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key",jsonResult);
		return  new ResponseEntity<String>(jsonResult,HttpStatus.OK) ;

	}
	
	/*
	    Request URL: http://localhost:9001/myhome3/entity3
		Request Method: GET
		Status Code: 200 
		
		Content-Length: 31
		Content-Type: application/json;charset=utf8
		*/
	@ResponseBody
	@RequestMapping(value = "entity3", produces = "application/json; charset=utf8")
	public String entity3() {

		// Json 결과값 : {"name": "홍길동","age": 21}
		String jsonResult = "{\"name\": \"홍길동\",\"age\": 21}";

		return jsonResult;
	}
	
}
