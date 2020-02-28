package com.youblog.review.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.youblog.review.api.IReview;
import com.youblog.review.service.ReviewService;
import com.youblog.review.service.dto.PostRanking;
import com.youblog.review.service.dto.ReviewDTO;
import com.youblog.util.exceptions.NotFoundException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.error;

@RestController
public class ReviewController implements IReview {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ReviewService service;

	public Mono<PostRanking> getPostAvgRanking(Long id) {
		logger.info("Fetching Post Avg Ranking by Post" + id);
		return service.getPostAvgRanking(id).switchIfEmpty(Mono.just(new PostRanking())).log();

	}

	@Override
	public Flux<ReviewDTO> findByPostId(Long postId) {
		return service.findByPostId(postId);
	}

	@Override
	public Mono<ReviewDTO> findById(String reviewId) {
		return service.findById(reviewId);
	}
}
