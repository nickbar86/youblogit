package com.youblog.review.persistence.dao;

import com.youblog.review.service.dto.PostRanking;

import reactor.core.publisher.Mono;

public interface ReviewDao {
	public Mono<PostRanking> getPostAvgRanking(Long postId);
}
