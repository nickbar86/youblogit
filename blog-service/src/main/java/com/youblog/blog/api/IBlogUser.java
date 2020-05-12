package com.youblog.blog.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.youblog.blog.services.dto.BlogUserDTO;
import com.youblog.blog.services.dto.BlogUserInfoDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
public interface IBlogUser {

	@PutMapping(value = "/blog-post/users", consumes = "application/json")
	public Mono<BlogUserInfoDTO> createNewUser(@RequestBody BlogUserDTO body);

	@PostMapping(value = "/blog-post/users", consumes = "application/json")
	public Mono<BlogUserInfoDTO> updateExistingUser(@RequestBody BlogUserDTO body);

	@DeleteMapping(value = "/blog-post/users/{userId}")
	public Mono<Void> deleteUser(@PathVariable int userId);

	@GetMapping(value = "/blog-post/users")
	public Flux<BlogUserInfoDTO> fetchUsers();

	@GetMapping(value = "/blog-post/users/profile")
	public Mono<BlogUserInfoDTO> fetchSignedInUser();

	// @CrossOrigin(origins = "http://localhost:3000")
	// @PutMapping(value = "/blog-post/users/{userId}/enable/{enable}", consumes =
	// "application/json")
	// public Mono<Boolean> enableUser(@PathVariable int userId,@PathVariable
	// boolean enable);
}
