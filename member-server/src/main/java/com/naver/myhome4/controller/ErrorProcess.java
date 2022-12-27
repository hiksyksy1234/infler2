package com.naver.myhome4.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ErrorProcess implements ErrorController {
	private static final Logger logger = LoggerFactory.getLogger(ErrorProcess.class);
    
	@RequestMapping(value = "/error")
    public String error()  {
    	logger.info("error"); 
		return "index.html";
       
    }
}
