package com.youblog.posts.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.youblog.posts.persistence.model.Post;
import com.youblog.posts.persistence.repository.PostRepository;
import com.youblog.posts.service.dto.PostDTO;
import com.youblog.posts.service.mappers.PostMapper;
import com.youblog.util.exceptions.NotFoundException;

@Service
@Transactional
public class PostService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PostRepository repository;

	@Autowired
	private Environment environment;

	@Autowired
	private PostMapper mapper;

	public Page<PostDTO> retrievePosts(Pageable pageable) {
		logger.info("Fetching All Posts");
		Page<Post> page = repository.findAllByOrderByDatePostedDesc(pageable);
		String port = environment.getProperty("local.server.port");
		if (port != null) {
			page.forEach(post -> post.setPort(Integer.parseInt(port)));
		}
		Page<PostDTO> responses = page.map(post -> mapper.entityToApi(post));

		return responses;
	}

	public PostDTO getPost(Long id) {
		logger.info("Fetching Post " + id);
		Post entity = repository.findById(id)
				.orElseThrow(() -> new NotFoundException("No Post found for postId: " + id));
		PostDTO response = mapper.entityToApi(entity);

		return response;
	}

	public PostDTO savePost(PostDTO post) {
		logger.info("Saving Post " + post.toString());
		post.setDatePosted(LocalDateTime.now());
		PostDTO updatedResp = mapper.entityToApi(repository.save(mapper.apiToEntity(post)));
		return updatedResp;
	}

	public void deletePost(Long id) {
		repository.deleteById(id);
	}

	public PostDTO updatePost(PostDTO data) {
		logger.info("Saving Post: {} ", data.getId());
		Post entity = repository.findById(data.getId())
				.orElseThrow(() -> new NotFoundException("No Post found for postId: " + data.getId()));
		entity.setContent(data.getContent());
		entity.setSummary(data.getSummary());
		entity.setTitle(data.getTitle());
		entity.setDatePosted(LocalDateTime.now());
		repository.save(entity);
		return data;
	}
}
