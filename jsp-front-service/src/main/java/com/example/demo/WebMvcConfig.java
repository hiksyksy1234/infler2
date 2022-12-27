package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	
	/*
	 * 여기서 중요한 점은 src 경로인데, spring boot는 기본적으로  /META-INF/resources/, /resources, /static, /public 경로를 기본탐색 한다고 합니다. 
	 * (https://suzxc2468.tistory.com/m/191?category=1044656글 발췌)
	 */
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS
	= {
			"classpath:/static/", "classpath:/resources/", 
			"classpath:/META-INF/resources"
			
	};

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//url mapping "/"로 접속하면 "/member/login"로 이동합니다.
		registry.addViewController("/").setViewName("forward:/jsp/login");
		//registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
	
	
}
