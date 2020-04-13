package com.youblog.blog.api;

import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.youblog.blog.services.dto.BlogUserDTO;
import com.youblog.blog.services.dto.BlogUserInfoDTO;
import com.youblog.blog.services.dto.PostDTO;
import com.youblog.blog.services.dto.PostRankingDTO;
import com.youblog.blog.services.dto.ReviewDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBlog {

	@GetMapping(value = "/blog-post/", produces = "application/json")
	public Mono<ResponseEntity> getPosts(
			@RequestParam(name = "page", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(name = "size", required = true, defaultValue = "10") int pageSize,
			@RequestParam(name = "sort", required = true, defaultValue = "id") String sort,
			@RequestParam(name = "direction", required = true, defaultValue = "DESC") Sort.Direction direction);

	@GetMapping(value = "/blog-post/posts/{postId}/", produces = "application/json")
	public Mono<PostRankingDTO> getPost(@PathVariable Integer postId, Integer delay, Integer faultPercent);

	@PutMapping(value = "/blog-post/posts/", consumes = "application/json")
	public Mono<Void> createPost(@RequestBody PostDTO body);

	@PostMapping(value = "/blog-post/posts/", consumes = "application/json")
	public Mono<PostDTO> updatePost(@RequestBody PostDTO body);

	@DeleteMapping(value = "/blog-post/posts/{postId}/")
	public Mono<Void> deletePost(@PathVariable int postId);

	@GetMapping(value = "/blog-post/reviews/", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ReviewDTO> getReviews(@RequestParam(name = "postId", required = true) Long postId);

	@GetMapping(value = "/blog-post/reviews/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ReviewDTO> getReview(@PathVariable String reviewId);

	@PutMapping(value = "/blog-post/reviews/", consumes = "application/json")
	public Mono<Void> createReview(@RequestBody ReviewDTO body);

	@PostMapping(value = "/blog-post/reviews/", consumes = "application/json")
	public Mono<ReviewDTO> updateReview(@RequestBody ReviewDTO body);

	@DeleteMapping(value = "/blog-post/reviews/{reviewId}/")
	public Mono<Void> deleteReview(@PathVariable int reviewId);

	@PutMapping(value = "/blog-post/users/", consumes = "application/json")
	public Mono<BlogUserInfoDTO> createNewUser(@RequestBody BlogUserDTO body);

	@PostMapping(value = "/blog-post/users/", consumes = "application/json")
	public Mono<BlogUserInfoDTO> updateExistingUser(@RequestBody BlogUserDTO body);

	@DeleteMapping(value = "/blog-post/users/{userId}/")
	public Mono<Void> deleteUser(@PathVariable int userId);

	@GetMapping(value = "/blog-post/users/", consumes = "application/json")
	public Flux<BlogUserInfoDTO> fetchUsers();

	// @CrossOrigin(origins = "http://localhost:3000")
	// @PutMapping(value = "/blog-post/users/{userId}/enable/{enable}", consumes =
	// "application/json")
	// public Mono<Boolean> enableUser(@PathVariable int userId,@PathVariable
	// boolean enable);

}
