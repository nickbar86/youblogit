package com.youblog.blog.services;

import com.youblog.blog.services.dto.PostRanking;
import com.youblog.blog.services.dto.ReviewDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewIntegrationService extends IntegrationService<ReviewDTO>{
	public Flux<ReviewDTO> getAll(long postId);
	public Mono<PostRanking> getPostAvgRanking(long postId);
	public void deletePostReviews(int postId);
}
