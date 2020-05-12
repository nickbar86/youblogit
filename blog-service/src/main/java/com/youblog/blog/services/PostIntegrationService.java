package com.youblog.blog.services;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.youblog.blog.services.dto.PostDTO;
import com.youblog.blog.services.dto.PostRanking;

import reactor.core.publisher.Mono;

public interface PostIntegrationService extends IntegrationService<PostDTO>{
	public Mono<ClientResponse> getAll(int pageIndex, int pageSize, String sort, Direction direction);
	public Mono<ClientResponse> getPostsByUserId(Long id, int pageIndex, int pageSize, String sort, Direction direction);
}
