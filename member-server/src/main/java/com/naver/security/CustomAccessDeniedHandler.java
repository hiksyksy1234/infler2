package com.naver.security;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Service;

public class CustomAccessDeniedHandler  implements AccessDeniedHandler{
	private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		logger.info(accessDeniedException.getMessage());
		logger.error("Access Denied Handler");
		response.sendRedirect(request.getContextPath()  + "/error/denied");
	}
}
