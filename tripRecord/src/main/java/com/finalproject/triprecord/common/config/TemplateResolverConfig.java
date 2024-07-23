package com.finalproject.triprecord.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class TemplateResolverConfig {

	
	@Bean
	public ClassLoaderTemplateResolver dotPlaResolver() {
		// prefix suffix 역할을 하는 클래스 메소드
		
		ClassLoaderTemplateResolver dotPla = new ClassLoaderTemplateResolver();
		dotPla.setPrefix("templates/views/place/");
		dotPla.setSuffix(".html");
		dotPla.setTemplateMode(TemplateMode.HTML);
		dotPla.setCharacterEncoding("UTF-8");
		dotPla.setCacheable(false);
		dotPla.setCheckExistence(true);
		
		return dotPla;
	}
	
	@Bean
	public ClassLoaderTemplateResolver dotMeResolver() {
		// prefix suffix 역할을 하는 클래스 메소드
		
		ClassLoaderTemplateResolver dotMe = new ClassLoaderTemplateResolver();
		dotMe.setPrefix("templates/views/member/");
		dotMe.setSuffix(".html");
		dotMe.setTemplateMode(TemplateMode.HTML);
		dotMe.setCharacterEncoding("UTF-8");
		dotMe.setCacheable(false);
		dotMe.setCheckExistence(true);
		
		return dotMe;
	}
}
