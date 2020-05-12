package com.youblog.blog.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.youblog.blog.services.dto.ReviewDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
public interface IBlogReview {
	@GetMapping(value = "/blog-post/reviews", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ReviewDTO> getReviews(@RequestParam(name = "postId", required = true) Long postId);

	@GetMapping(value = "/blog-post/reviews/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ReviewDTO> getReview(@PathVariable Integer reviewId);

	@PutMapping(value = "/blog-post/reviews", consumes = "application/json")
	public Mono<Void> createReview(@RequestBody ReviewDTO body);

	@PostMapping(value = "/blog-post/reviews", consumes = "application/json")
	public Mono<ReviewDTO> updateReview(@RequestBody ReviewDTO body);

	@DeleteMapping(value = "/blog-post/reviews/{reviewId}")
	public Mono<Void> deleteReview(@PathVariable int reviewId);
}
