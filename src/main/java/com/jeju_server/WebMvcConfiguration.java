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
				.allowedOriginPatterns("http://43.203.142.206", "http://localhost:3000")
				.allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}