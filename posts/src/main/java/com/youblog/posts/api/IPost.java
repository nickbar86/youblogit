package com.youblog.posts.api;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.youblog.posts.service.dto.PostDTO;

@RequestMapping("posts")
public interface IPost {
	@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "Link")
	@GetMapping(path = "/")
	public ResponseEntity<List<PostDTO>> retrievePosts(Pageable pageable) ;

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping(path = "{id}", produces = "application/json")
	public ResponseEntity<PostDTO> getPost(@PathVariable("id") Long id) ;

	@CrossOrigin(origins = "http://localhost:3000")
	@PutMapping(path = "/", produces = "application/json")
	public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO post) ;
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path = "/", produces = "application/json")
	public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO post) ;
}
