package com.youblog.blog.controllers;

import org.springframework.integration.handler.advice.RequestHandlerCircuitBreakerAdvice.CircuitBreakerOpenException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.RestController;

import com.youblog.blog.api.IBlogUser;
import com.youblog.blog.services.UserIntegrationService;
import com.youblog.blog.services.dto.BlogUserDTO;
import com.youblog.blog.services.dto.BlogUserInfoDTO;

import io.github.resilience4j.reactor.retry.RetryExceptionWrapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class BlogUserController extends AbstractConroller implements IBlogUser{
	private final UserIntegrationService userIntegrationService;
	
	
	public BlogUserController(UserIntegrationService userIntegrationService) {
		super();
		this.userIntegrationService = userIntegrationService;
	}

	@Override
	public Mono<BlogUserInfoDTO> createNewUser(BlogUserDTO body) {
		return userIntegrationService.create(body).onErrorMap(RetryExceptionWrapper.class, RetryExceptionWrapper::getCause)
				.onErrorReturn(CircuitBreakerOpenException.class, getUserFallbackValue());
	}

	private BlogUserInfoDTO getUserFallbackValue() {
		return new BlogUserInfoDTO();
	}
	
	@Override
	public Mono<BlogUserInfoDTO> updateExistingUser(BlogUserDTO body) {
		return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
				.flatMap(auth -> userIntegrationService.getUserByEmail(auth.getName())).flatMap(user -> {
					body.setId(user.getId());
					body.setRole(user.getRole());
					body.setEnabled(user.isEnabled());
					return userIntegrationService.update(body);
				}).onErrorMap(RetryExceptionWrapper.class, RetryExceptionWrapper::getCause)
				.onErrorReturn(CircuitBreakerOpenException.class, getUserFallbackValue());
	}

	@Override
	public Mono<Void> deleteUser(int userId) {
		return userIntegrationService.deleteUser(userId)
				.onErrorMap(RetryExceptionWrapper.class, RetryExceptionWrapper::getCause);
	}

	@Override
	public Flux<BlogUserInfoDTO> fetchUsers() {
		return userIntegrationService.getAll();
	}

	@Override
	public Mono<BlogUserInfoDTO> fetchSignedInUser() {
		return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
				.flatMap(auth -> userIntegrationService.getUserByEmail(auth.getName()))
				.onErrorMap(RetryExceptionWrapper.class, RetryExceptionWrapper::getCause)
				.onErrorReturn(CircuitBreakerOpenException.class, getUserFallbackValue());

	}

}
