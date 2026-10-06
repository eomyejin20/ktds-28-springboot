package com.ktdsuniversity.edu.commons.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ktdsuniversity.edu.commons.beans.interceptors.CheckSessionInterceptor;

/**
 * 인터셉터 등록을 위한 Spring Boot 설정 클래스 (application.yml에서 지원하지않는 Custom 설정)
 */
@Configuration // @Component상속
public class WebConfig implements WebMvcConfigurer{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 인터셉터 등록하기
		registry.addInterceptor(new CheckSessionInterceptor())
				.addPathPatterns("/**")
				// 세션 체크를 하지 않을 URL 패턴 정의
				.excludePathPatterns(
						"/members/login",
						"/members/signup",
						"/articles/list"
//						"/articles/{articleId}",
//						"/articles/{articleId}/replies/list"
						)
		;
		
	}

}
