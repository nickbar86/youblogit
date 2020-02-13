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

import com.microservices.blog.persistence.model.Post;
import com.microservices.blog.persistence.repository.PostRepository;
import com.microservices.blog.proxies.PostRanking;
import com.microservices.blog.proxies.ReviewsServiceProxy;
import com.microservices.blog.service.dto.PostDTO;
import com.microservices.blog.service.mappers.PostMapper;
import com.microservices.util.exceptions.NotFoundException;

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
	
	@Autowired
	private ReviewsServiceProxy serviceProxy;
	
	//TODO Fix Integration Testing to mock Feign client
	@Value("${spring.profiles.active}")
	private String activeProfile;

	public Page<PostDTO> retrievePosts(Pageable pageable) {
		logger.info("Fetching All Posts");
		Page<Post> page = repository.findAllByOrderByDatePostedDesc(pageable);
		String port = environment.getProperty("local.server.port");
		if (port != null) {
			page.forEach(post -> post.setPort(Integer.parseInt(port)));
		}
		Page<PostDTO> responses = page.map(post -> mapper.entityToApi(post));
		if(!activeProfile.equals("test")) {
			responses.forEach(post -> {
				java.util.Optional<PostRanking> ranking =  serviceProxy.getPostAvgRanking(post.getId());
				ranking.ifPresent(rank->post.setRanking(rank.getAvgRanking()));
			});
		}
		
		return responses;
	}

	public PostDTO getPost(@PathVariable("id") Long id) {
		logger.info("Fetching Post " + id);
		Post entity = repository.findById(id)
				.orElseThrow(() -> new NotFoundException("No Post found for postId: " + id));
		PostDTO response = mapper.entityToApi(entity);
		
		if(!activeProfile.equals("test")) {
			java.util.Optional<PostRanking> ranking =  serviceProxy.getPostAvgRanking(id);
			ranking.ifPresent(rank->response.setRanking(rank.getAvgRanking()));
		}
		return response;
	}

	public PostDTO savePost(@RequestBody PostDTO post) {
		logger.info("Saving Post " + post.toString());
		post.setDatePosted(LocalDateTime.now());
		PostDTO updatedResp = mapper.entityToApi(repository.save(mapper.apiToEntity(post)));
		
		if(!activeProfile.equals("test")) {
			java.util.Optional<PostRanking> ranking =  serviceProxy.getPostAvgRanking(post.getId());
			ranking.ifPresent(rank->updatedResp.setRanking(rank.getAvgRanking()));
		}
		return updatedResp;
	}
}
