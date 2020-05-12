package com.youblog.blog.services;

import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.youblog.blog.services.dto.BlogUserDTO;
import com.youblog.blog.services.dto.BlogUserDetails;
import com.youblog.blog.services.dto.BlogUserInfoDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserIntegrationService{
	public Flux<BlogUserInfoDTO> getAll();

	Mono<BlogUserInfoDTO> update(BlogUserDTO body);

	Mono<BlogUserInfoDTO> create(BlogUserDTO body);
	
	public String getErrorMessage(WebClientResponseException ex);

	public Throwable handleException(Throwable ex);

	public Mono<BlogUserDetails> get(Integer id);

	public Mono<Void> deleteUser(int userId);
	
	public Mono<BlogUserInfoDTO> getUserByEmail(String email);
}
