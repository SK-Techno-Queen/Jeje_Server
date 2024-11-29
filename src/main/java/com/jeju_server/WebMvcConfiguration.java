package com.jeju_server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns("http://localhost:3000", "http://3.38.199.187:3000", "http://3.38.199.187:80")
				.allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}