package com.youblog.review.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.youblog.review.service.dto.PostRanking;
import com.youblog.review.service.dto.ReviewDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("reviews")
public interface IReview {
	@GetMapping(path = "/posts/{id}/rankings/avg", produces = "application/json")
	public Mono<PostRanking> getPostAvgRanking(@PathVariable("id") Long id);
	
	@GetMapping(path = "/", produces = "application/json")
	public Flux<ReviewDTO> findByPostId(@RequestParam(name = "postId", required = true) Long postId);
	
	@GetMapping(path = "/{id}/", produces = "application/json")
	public Mono<ReviewDTO> findById(@PathVariable(name = "id") String reviewId);
}
