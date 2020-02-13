package com.microservices.blog.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.blog.service.PostService;
import com.microservices.blog.service.dto.PostDTO;
import com.microservices.blog.util.PaginationUtil;
import com.microservices.util.exceptions.NotFoundException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("posts")
public class PostController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PostService service;
	

	@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "Link")
	@GetMapping(path = "/")
	@ApiOperation(value = "${api.posts.Get.all.description}", notes = "${api.posts.Get.all.notes}")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
			@ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fails. See response message for more information.") })
	public ResponseEntity<List<PostDTO>> retrievePosts(Pageable pageable) {
		logger.info("Fetching All Posts");
		Page<PostDTO> page = service.retrievePosts(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/posts");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@ApiOperation(value = "${api.posts.Get.post.description}", notes = "${api.posts.Get.post.notes}")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
			@ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fails. See response message for more information.") })
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping(path = "{id}", produces = "application/json")
	public ResponseEntity<PostDTO> getPost(@PathVariable("id") Long id) {
		logger.info("Fetching Post by " + id);
		try {
			return ResponseEntity.status(HttpStatus.OK).body(service.getPost(id));
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}

	}

	@ApiOperation(value = "${api.posts.Post.post.description}", notes = "${api.posts.Post.post.notes}")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
			@ApiResponse(code = 404, message = "Not found, the specified id does not exist."),
			@ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fails. See response message for more information.") })
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path = "/", produces = "application/json")
	public ResponseEntity<PostDTO> savePost(@RequestBody PostDTO post) {
		logger.info("Saving Post " + post.toString());
		return ResponseEntity.status(HttpStatus.OK).body(service.savePost(post));
	}
}
