package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 정적 리소스 매핑:
 *  - /upload/** URL로 접근하면 로컬 디렉토리(app.upload.dir)에서 파일을 서빙
 *  - 예) http://localhost:8080/upload/프로필파일명.jpg
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	// application.yml 의 app.upload.dir 값을 주입
	@Value("${app.upload.dir}")
	private String uploadDir;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Windows/Unix 모두 동작: file:<절대경로>/ 형태로 매핑
		registry
				.addResourceHandler("/upload/**")
				.addResourceLocations("file:" + (uploadDir.endsWith("/") ? uploadDir : uploadDir + "/"));
	}
}
