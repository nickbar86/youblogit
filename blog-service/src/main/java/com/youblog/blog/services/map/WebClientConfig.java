package com.youblog.blog.services.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

@Component
public class WebClientConfig {

	private String systemAuthUsername;
	private String systemAuthPassword;
	private final WebClient.Builder webClientBuilder;
	private WebClient webClient;
	private WebClient authenticatedWebClient;

	@Autowired
	public WebClientConfig(
			@Value("${app.system-auth-username}") String systemAuthUsername,
			@Value("${app.system-auth-password}") String systemAuthPassword, 
			Builder webClientBuilder) {
		super();
		this.systemAuthUsername = systemAuthUsername;
		this.systemAuthPassword = systemAuthPassword;
		this.webClientBuilder = webClientBuilder;
	}

	@Bean
	@Qualifier("simpleWebClient")
	public WebClient getSimpleWebClient() {
		if (webClient == null) {
			webClient = webClientBuilder.build();
		}
		return webClient;
	}
	
	@Bean
	@Qualifier("authenticatedWebClient")
	public WebClient getAuthenticatedWebClient() {
		if (authenticatedWebClient == null) {
			authenticatedWebClient = webClientBuilder
					.filter(ExchangeFilterFunctions.basicAuthentication(systemAuthUsername, systemAuthPassword))
					.build();
		}
		return authenticatedWebClient;
	}
}
