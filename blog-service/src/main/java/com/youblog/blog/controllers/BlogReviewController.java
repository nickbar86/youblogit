package com.youblog.blog.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.RestController;

import com.youblog.blog.api.IBlogReview;
import com.youblog.blog.services.ReviewIntegrationService;
import com.youblog.blog.services.dto.ReviewDTO;
import com.youblog.util.exceptions.InternalApplicationException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class BlogReviewController extends AbstractConroller implements IBlogReview{
	
	private static final Logger LOG = LoggerFactory.getLogger(BlogReviewController.class);
	
	private final ReviewIntegrationService integration;
	
	public BlogReviewController(ReviewIntegrationService integration) {
		super();
		this.integration = integration;
	}

	@Override
	public Flux<ReviewDTO> getReviews(Long postId) {
		return integration.getAll(postId);
	}

	@Override
	public Mono<ReviewDTO> getReview(Integer reviewId) {
		return integration.get(reviewId);
	}

	@Override
	public Mono<Void> createReview(ReviewDTO body) {
		return ReactiveSecurityContextHolder.getContext().doOnSuccess(sc -> internalCreateReview(sc, body)).then();
	}

	public void internalCreateReview(SecurityContext sc, ReviewDTO body) {
		try {
			logAuthorizationInfo(sc);
			LOG.debug("internalCreateReview: creates a Review for post: {}", body.getPostId());
			body.setUserId(Optional.ofNullable(getUserIdFromSC(sc))
					.orElseThrow(() -> new AccessDeniedException(NO_USER_ID)).intValue());
			integration.create(body);
			LOG.debug("internalCreateReview: created a Review for post: {}", body.getPostId());
		} catch (RuntimeException re) {
			throw new InternalApplicationException(re);
		}
	}
	
	@Override
	public Mono<ReviewDTO> updateReview(ReviewDTO body) {
		return ReactiveSecurityContextHolder.getContext().doOnSuccess(sc -> internalUpdateReview(sc, body))
				.thenReturn(body);
	}

	public void internalUpdateReview(SecurityContext sc, ReviewDTO body) {
		try {
			logAuthorizationInfo(sc);
			LOG.debug("internalUpdateReview: updates an existing review:{}", body.getReviewId());
			integration.update(body);
			LOG.debug("internalUpdateReview: updated an existing review:{}", body.getReviewId());
		} catch (RuntimeException re) {
			throw new InternalApplicationException(re);
		}
	}

	@Override
	public Mono<Void> deleteReview(int reviewId) {
		return ReactiveSecurityContextHolder.getContext().doOnSuccess(sc -> internalDeleteReview(sc, reviewId)).then();
	}

	private void internalDeletePostReviews(int postId) {
		try {
			LOG.debug("internalDeletePostReviews: Deletes a reviews for postId: {}", postId);
			integration.deletePostReviews(postId);
			LOG.debug("internalDeleteReview: Deletes review with reviewId: {}", postId);
		} catch (RuntimeException re) {
			throw new InternalApplicationException(re);
		}
	}

	private void internalDeleteReview(SecurityContext sc, int reviewId) {
		try {
			logAuthorizationInfo(sc);
			LOG.debug("internalDeleteReview: Deletes a review aggregate for reviewId: {}", reviewId);
			integration.delete(reviewId);
			LOG.debug("internalDeleteReview: Deletes review with reviewId: {}", reviewId);
		} catch (RuntimeException re) {
			throw new InternalApplicationException(re);
		}
	}
}
