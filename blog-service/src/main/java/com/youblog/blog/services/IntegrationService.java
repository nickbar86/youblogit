package com.youblog.blog.services;

import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

public interface IntegrationService<T> {

	public String getErrorMessage(WebClientResponseException ex);

	public Throwable handleException(Throwable ex);

	public Mono<T> get(Integer id);

	public Mono<Void> create(T body);

	public T update(T body);

	public void delete(int id);
}
