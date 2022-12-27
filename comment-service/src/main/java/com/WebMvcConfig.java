package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//url mapping "/"로 접속하면 "/member/login"로 이동합니다.
		registry.addViewController("/").setViewName("forward:/login");
	}

	/*
	 * @Autowired JwtInterceptor jwtInterceptor;
	 */
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
        .addResourceLocations("classpath:/static/", "classpath:/templates/");
    }
	/*
	 * @Override public void addInterceptors(InterceptorRegistry registry) {
	 * registry.addInterceptor(jwtInterceptor) .addPathPatterns("/boards/new")//
	 * Interceptor가 적용될 경로 .excludePathPatterns(new String[]{"/login","/join"}); //
	 * Interceptor가 적용되지 않을 경로 }
	 */
	
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**")
//        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
//        .allowedOrigins("http://localhost:8000");
//	}
//	
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOrigins("*")
        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
       // .allowedOrigins("http://localhost:8081");
	}

	
}







/*@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}*/