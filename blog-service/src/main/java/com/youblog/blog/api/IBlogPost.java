package com.youblog.blog.api;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.youblog.blog.services.dto.PostDTO;
import com.youblog.blog.services.dto.PostRankingDTO;

import reactor.core.publisher.Mono;

@RestController()
public interface IBlogPost {
	@GetMapping(value = "/blog-post/posts", produces = "application/json")
	public Mono<ResponseEntity> getPosts(
			@RequestParam(name = "userPosts", required = false, defaultValue = "false") boolean userPosts,
			@RequestParam(name = "page", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(name = "size", required = true, defaultValue = "10") int pageSize,
			@RequestParam(name = "sort", required = true, defaultValue = "id") String sort,
			@RequestParam(name = "direction", required = true, defaultValue = "DESC") Sort.Direction direction);

	@GetMapping(value = "/blog-post/posts/{postId}", produces = "application/json")
	public Mono<PostRankingDTO> getPost(@PathVariable Integer postId, Integer delay, Integer faultPercent);

	@PutMapping(value = "/blog-post/posts", consumes = "application/json")
	public Mono<Void> createPost(@RequestBody PostDTO body);

	@PostMapping(value = "/blog-post/posts", consumes = "application/json")
	public Mono<PostDTO> updatePost(@RequestBody PostDTO body);

	@DeleteMapping(value = "/blog-post/posts/{postId}")
	public Mono<Void> deletePost(@PathVariable int postId);
}
