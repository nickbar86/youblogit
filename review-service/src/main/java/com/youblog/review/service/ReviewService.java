package com.youblog.review.service;

import java.time.LocalDateTime;

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
		LOG.info("Fetching Avg Ranking Score for post:{}" ,postId);
		return repository.getPostAvgRanking(postId);
	}

	public Flux<ReviewDTO> findByPostId(Long postId) {
		if (postId < 1)
			throw new InvalidInputException("Invalid postId: " + postId);
		Flux<Review> entityList = repository.findByPostId(postId).doOnNext(inp -> LOG.debug(inp.getId().toHexString()));
		Flux<ReviewDTO> list = entityList.map(ent -> mapper.entityToApi(ent)).log();
		LOG.debug("findByPostId: {}", postId);
		return list;
	}

	public Mono<ReviewDTO> findById(String reviewId) {
		if (reviewId == null)
			throw new InvalidInputException("Invalid reviewId: " + reviewId);
		Mono<Review> review = repository.findById(reviewId);
		Mono<ReviewDTO> reviewDTO = review.map(ent -> {
			return mapper.entityToApi(ent);
		});
		LOG.debug("findById: {}", reviewId);
		return reviewDTO;
	}

	public void saveReview(ReviewDTO review) {
		repository.save(mapper.apiToEntity(review));
	}

	public void deleteReviewsByPostId(long postId) {
		repository.deleteByPostId(postId);
	}

	public void deleteReview(long reviewId) {
		repository.deleteById(reviewId);
	}

	public void updateReview(ReviewDTO data) {
		LOG.info("Saving Review: {} ", data.getReviewId());
		repository.findById(data.getReviewId()).doOnNext(entity -> {
			entity.setDatePosted(LocalDateTime.now());
			entity.setRanking(data.getRanking());
			entity.setReview(data.getReview());
			repository.save(entity);
		});
	}
}
