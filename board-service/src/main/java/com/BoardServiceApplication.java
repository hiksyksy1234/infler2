package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import feign.Logger;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients //comment-service의 정보 수정 관련 호출하기 위한 것
public class BoardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardServiceApplication.class, args);
	}
	
	//FeignClient 로그를 위한 빈을 작성합니다.
	@Bean
	public Logger.Level feignLoggerLevel(){
		return Logger.Level.FULL;
	}

}
