package com.naver.myhome4.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController  {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
	@RequestMapping(value = "/list")
    public String list()  {
    	logger.info("list"); 
		return "redirect:boards";
       
    }
}
