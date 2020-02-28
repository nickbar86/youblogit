package com.youblog.review.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.youblog.review.persistence.dao.ReviewDao;
import com.youblog.review.persistence.model.Review;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewRepository extends ReactiveCrudRepository<Review, Long>, ReviewDao {
	Mono<Review> findById(String id);

	Flux<Review> findAllByPostIdOrderByDatePostedDesc(Long postId);

	//Mono<Review> findByReviewId(Long id);

	Flux<Review> findByPostId(Long postId);
	
	void deleteByPostId(Long postId);
}