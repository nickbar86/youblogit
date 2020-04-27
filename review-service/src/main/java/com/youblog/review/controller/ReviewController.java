package com.youblog.review.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.youblog.review.api.IReview;
import com.youblog.review.service.ReviewService;
import com.youblog.review.service.dto.PostRanking;
import com.youblog.review.service.dto.ReviewDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ReviewController implements IReview {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ReviewService service;

	public Mono<PostRanking> getPostAvgRanking(Long id) {
		logger.info("Fetching Post Avg Ranking by Post:{}", id);
		return service.getPostAvgRanking(id).switchIfEmpty(Mono.just(new PostRanking())).log();

	}

	@Override
	public Flux<ReviewDTO> findByPostId(Long postId) {
		return service.findByPostId(postId);
	}

	@Override
	public Mono<ReviewDTO> findById(String reviewId) {
		return service.findById(reviewId);
	}
}
