package com.finalproject.triprecord.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class TemplateResolverConfig {

	@Bean
	public ClassLoaderTemplateResolver dotResolver() {
		ClassLoaderTemplateResolver dot = new ClassLoaderTemplateResolver();
		dot.setPrefix("templates/views/");
		dot.setSuffix(".html");
		dot.setTemplateMode(TemplateMode.HTML);
		dot.setCharacterEncoding("UTF-8");
		dot.setCacheable(false);
		dot.setCheckExistence(true);
		
		return dot;
	}
	
	@Bean
	public ClassLoaderTemplateResolver dotPlaResolver() {
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
		ClassLoaderTemplateResolver dotMe = new ClassLoaderTemplateResolver();
		dotMe.setPrefix("templates/views/member/");
		dotMe.setSuffix(".html");
		dotMe.setTemplateMode(TemplateMode.HTML);
		dotMe.setCharacterEncoding("UTF-8");
		dotMe.setCacheable(false);
		dotMe.setCheckExistence(true);
		
		return dotMe;
	}
	
	@Bean
	public ClassLoaderTemplateResolver dotBoResolver() {
		ClassLoaderTemplateResolver dotBo = new ClassLoaderTemplateResolver();
		dotBo.setPrefix("templates/views/community/");
		dotBo.setSuffix(".html");
		dotBo.setTemplateMode(TemplateMode.HTML);
		dotBo.setCharacterEncoding("UTF-8");
		dotBo.setCacheable(false);
		dotBo.setCheckExistence(true);
		
		return dotBo;
	}
	
	@Bean
	public ClassLoaderTemplateResolver dotNoResolver() {
		ClassLoaderTemplateResolver dotNo = new ClassLoaderTemplateResolver();
		dotNo.setPrefix("templates/views/service/");
		dotNo.setSuffix(".html");
		dotNo.setTemplateMode(TemplateMode.HTML);
		dotNo.setCharacterEncoding("UTF-8");
		dotNo.setCacheable(false);
		dotNo.setCheckExistence(true);
		
		return dotNo;
	}
	
	@Bean
	public ClassLoaderTemplateResolver dotMaResolver() {
		ClassLoaderTemplateResolver dotMa = new ClassLoaderTemplateResolver();
		dotMa.setPrefix("templates/views/matching/");
		dotMa.setSuffix(".html");
		dotMa.setTemplateMode(TemplateMode.HTML);
		dotMa.setCharacterEncoding("UTF-8");
		dotMa.setCacheable(false);
		dotMa.setCheckExistence(true);
		
		return dotMa;
	}
	
	@Bean
	public ClassLoaderTemplateResolver dotAdResolver() {
		ClassLoaderTemplateResolver dotAd = new ClassLoaderTemplateResolver();
		dotAd.setPrefix("templates/views/admin/");
		dotAd.setSuffix(".html");
		dotAd.setTemplateMode(TemplateMode.HTML);
		dotAd.setCharacterEncoding("UTF-8");
		dotAd.setCacheable(false);
		dotAd.setCheckExistence(true);
		
		return dotAd;
	}
	
	@Bean
	public ClassLoaderTemplateResolver dotplResolver() {
		ClassLoaderTemplateResolver dotpl = new ClassLoaderTemplateResolver();
		dotpl.setPrefix("templates/views/plan/");
		dotpl.setSuffix(".html");
		dotpl.setTemplateMode(TemplateMode.HTML);
		dotpl.setCharacterEncoding("UTF-8");
		dotpl.setCacheable(false);
		dotpl.setCheckExistence(true);
		
		return dotpl;
	}
}
