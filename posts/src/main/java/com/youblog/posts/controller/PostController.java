package com.youblog.posts.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.youblog.posts.api.IPost;
import com.youblog.posts.service.PostService;
import com.youblog.posts.service.dto.PostDTO;
import com.youblog.posts.util.PaginationUtil;
import com.youblog.util.exceptions.NotFoundException;

@RestController
public class PostController implements IPost {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PostService service;

	@Override
	public ResponseEntity<List<PostDTO>> retrievePosts(Pageable pageable, Integer blogUserId) {
		logger.info("Fetching All Posts");
		Page<PostDTO> page = service.retrievePosts(pageable,blogUserId);
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("blogUserId", blogUserId);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/posts/", Optional.of(queryParams));
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<PostDTO> getPost(Long id) {
		logger.info("Fetching Post by id" + id);
		try {
			return ResponseEntity.status(HttpStatus.OK).body(service.getPost(id));
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}

	}

	@Override
	public ResponseEntity<PostDTO> createPost(PostDTO post) {
		logger.info("Saving Post " + post.toString());
		return ResponseEntity.status(HttpStatus.CREATED).body(service.savePost(post));
	}

	@Override
	public ResponseEntity<PostDTO> updatePost(PostDTO post) {
		logger.info("Saving Post " + post.toString());
		return ResponseEntity.status(HttpStatus.OK).body(service.updatePost(post));
	}
}
