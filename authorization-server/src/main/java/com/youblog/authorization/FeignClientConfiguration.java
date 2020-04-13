package com.youblog.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.auth.BasicAuthRequestInterceptor;

@Configuration
public class FeignClientConfiguration {
	private String systemAuthUsername;
	private String systemAuthPassword;

	public FeignClientConfiguration(@Value("${app.system-auth-username}") String systemAuthUsername,
			@Value("${app.system-auth-password}") String systemAuthPassword) {
		super();
		this.systemAuthUsername = systemAuthUsername;
		this.systemAuthPassword = systemAuthPassword;
	}

	@Bean
	public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
		return new BasicAuthRequestInterceptor(systemAuthUsername, systemAuthPassword);
	}
}
