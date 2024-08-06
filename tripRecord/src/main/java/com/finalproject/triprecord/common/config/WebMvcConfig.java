package com.finalproject.triprecord.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.finalproject.triprecord.common.interceptor.AdminInterceptor;
import com.finalproject.triprecord.common.interceptor.CheckLoginInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**") // 매핑할 URI 이름 설정(접근할 패턴 매핑)
				.addResourceLocations("classpath:static/");
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AdminInterceptor())
				.addPathPatterns("/*.ad");
		
		registry.addInterceptor(new CheckLoginInterceptor())
		.addPathPatterns("/*.pl")
		.addPathPatterns("/updateReviewView.pla", "/updatePlaReview.pla", "/insertPlaReview.pla", "/placeReviewWrite.pla");
	}
}
