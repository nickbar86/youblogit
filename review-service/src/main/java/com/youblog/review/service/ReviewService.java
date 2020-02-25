package com.youblog.review.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youblog.review.mappers.ReviewMapper;
import com.youblog.review.persistence.model.Review;
import com.youblog.review.persistence.repository.ReviewRepository;
import com.youblog.review.service.dto.PostRanking;
import com.youblog.review.service.dto.ReviewDTO;
import com.youblog.util.exceptions.InvalidInputException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ReviewService {
	private static final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

	private ReviewRepository repository;
	private final ReviewMapper mapper;

	@Autowired
	public ReviewService(ReviewRepository repository, ReviewMapper mapper) {
		super();
		this.repository = repository;
		this.mapper = mapper;
	}

	public Mono<PostRanking> getPostAvgRanking(Long postId) {
		LOG.info("Fetching Avg Ranking Score for post:" + postId);
		return repository.getPostAvgRanking(postId);
	}

	public Flux<ReviewDTO> findByPostId(Long postId) {
		if (postId < 1)
			throw new InvalidInputException("Invalid postId: " + postId);
		Flux<Review> entityList = repository.findByPostId(postId);
		Flux<ReviewDTO> list = entityList.map(ent -> mapper.entityToApi(ent));
		LOG.debug("findByPostId: {}", postId);
		return list;
	}

	public Mono<ReviewDTO> findById(Long reviewId) {
		if (reviewId < 1)
			throw new InvalidInputException("Invalid reviewId: " + reviewId);
		Mono<Review> review = repository.findById(reviewId.toString());
		Mono<ReviewDTO> reviewDTO = review.map(ent -> mapper.entityToApi(ent));
		LOG.debug("findById: {}", reviewDTO);
		return reviewDTO;
	}
}
