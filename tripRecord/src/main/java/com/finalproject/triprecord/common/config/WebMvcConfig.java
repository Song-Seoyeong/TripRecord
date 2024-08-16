package com.finalproject.triprecord.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.finalproject.triprecord.common.interceptor.AdminInterceptor;
import com.finalproject.triprecord.common.interceptor.CheckLoginInterceptor;
import com.finalproject.triprecord.common.interceptor.CheckPlannerSevenDaysSchedule;
import com.finalproject.triprecord.common.interceptor.DeleteNotification;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	
	private final CheckPlannerSevenDaysSchedule checkPlannerSevenDaysSchedule;
	
	 @Autowired
	    public WebMvcConfig(CheckPlannerSevenDaysSchedule checkPlannerSevenDaysSchedule) {
	        this.checkPlannerSevenDaysSchedule = checkPlannerSevenDaysSchedule;
	    }

	
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
		.addPathPatterns("/updateReviewView.pla", "/updatePlaReview.pla", "/insertPlaReview.pla", "/placeReviewWrite.pla")
		.addPathPatterns("/matchingRequest.ma", "/matchingReview.ma", "/matchingReviewView.ma")
		.addPathPatterns("/commuWrite.bo", "insertQuestion.no", "commuEdit.bo", "insertBoard.bo", "askWrite.no", "editQuestion.no");
        
        registry.addInterceptor(checkPlannerSevenDaysSchedule)
        		.addPathPatterns("/login.me");
        
        registry.addInterceptor(new DeleteNotification())
        		.addPathPatterns("/**")
        		.excludePathPatterns("/login.me");
	}
}
