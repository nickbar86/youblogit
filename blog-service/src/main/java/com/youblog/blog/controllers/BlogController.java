package com.youblog.blog.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.handler.advice.RequestHandlerCircuitBreakerAdvice.CircuitBreakerOpenException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youblog.blog.services.BlogPostsIntegration;
import com.youblog.blog.services.dto.BlogUserDTO;
import com.youblog.blog.services.dto.BlogUserDetails;
import com.youblog.blog.services.dto.BlogUserInfoDTO;
import com.youblog.blog.services.dto.PostDTO;
import com.youblog.blog.services.dto.PostDTO.PostDTOBuilder;
import com.youblog.blog.services.dto.PostRanking;
import com.youblog.blog.services.dto.PostRankingDTO;
import com.youblog.blog.services.dto.PostRankingDTO.PostRankingDTOBuilder;
import com.youblog.blog.services.dto.ReviewDTO;
import com.youblog.util.exceptions.InternalApplicationException;
import com.youblog.util.exceptions.InvalidInputException;
import com.youblog.util.exceptions.NotFoundException;
import com.youblog.util.http.HttpErrorInfo;
import com.youblog.util.http.ServiceUtil;

import io.github.resilience4j.reactor.retry.RetryExceptionWrapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
//"To be Removed after unit testing"
@RestController
@Deprecated
public class BlogController {
/*
	private static final String NO_USER_ID = "No User Id";

	private static final Logger LOG = LoggerFactory.getLogger(BlogController.class);

	private final SecurityContext nullSC = new SecurityContextImpl();
	private final ObjectMapper mapper;
	private final ServiceUtil serviceUtil;
	private final BlogPostsIntegration integration;

	@Autowired
	public BlogController(ServiceUtil serviceUtil, BlogPostsIntegration integration, ObjectMapper mapper) {
		this.serviceUtil = serviceUtil;
		this.integration = integration;
		this.mapper = mapper;
	}

	@Override
	public Mono<ResponseEntity> getPosts(
			@RequestParam(name = "userPosts", required = false, defaultValue = "false") boolean userPosts,
			@RequestParam(name = "page", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(name = "size", required = true, defaultValue = "10") int pageSize,
			@RequestParam(name = "sort", required = true, defaultValue = "id") String sort,
			@RequestParam(name = "direction", required = true, defaultValue = "DESC") Sort.Direction direction) {

		Mono<ClientResponse> postsResponse;
		if (userPosts) {
			postsResponse = ReactiveSecurityContextHolder.getContext().map(this::getUserIdFromSC)
					.flatMap(userId -> integration.getPostsByUserId(userId, pageIndex, pageSize, sort, direction));
		} else {
			postsResponse = integration.getPosts(pageIndex, pageSize, sort, direction);
		}
		Flux<PostDTO> posts = postsResponse.flatMapMany(response -> response.bodyToFlux(PostDTO.class).log());
		Flux<PostRanking> avgRankings = posts.flatMap(post -> integration.getPostAvgRanking(post.getId()));
		Flux<PostRankingDTO> postRankings = avgRankings.collectMap(PostRanking::getPostId, PostRanking::getAvgRanking)
				.flatMapMany(mapToPostRankingDTO(posts));
		return Mono.zip(values -> createPostsAggregate((ClientResponse) values[0], (List<PostRankingDTO>) values[1]),
				postsResponse, postRankings.collectList()).cast(ResponseEntity.class);
	}

	private Function<? super Map<Long, Float>, ? extends Publisher<? extends PostRankingDTO>> mapToPostRankingDTO(
			Flux<PostDTO> posts) {
		return rankings -> posts.handle((post, sink) -> {
			PostRankingDTOBuilder builder = PostRankingDTO
					.PostRankingDTOBuilder
					.withPostDto(post);
			if (rankings.keySet().contains(post.getId())) {
				sink.next(builder.ranking(rankings.get(post.getId())).build());
			} else {
				sink.next(builder.build());
			}
		});
	}

	private ResponseEntity<List<PostRankingDTO>> createPostsAggregate(ClientResponse response,
			List<PostRankingDTO> list) {
		List<String> links = response.headers().header("Link").stream().map(link -> link.replace("posts", "blog-post"))
				.collect(Collectors.toList());
		List<String> xTotalCount = response.headers().header("X-Total-Count");
		HttpHeaders headers = new HttpHeaders();
		headers.addAll("X-Total-Count", xTotalCount);
		headers.addAll("Link", links);
		return ResponseEntity.ok().headers(headers).body(list);
	}

	@Override
	public Mono<PostRankingDTO> getPost(@PathVariable Integer postId, Integer delay, Integer faultPercent) {
		Mono<PostRankingDTO> postRankingMono = Mono
				.zip(values -> createPostAggregate(
						(SecurityContext) values[0], (PostDTO) values[1], (PostRanking) values[2]),
						ReactiveSecurityContextHolder.getContext().defaultIfEmpty(nullSC),
						integration.getPost(postId, delay, faultPercent)
								.onErrorMap(RetryExceptionWrapper.class, RetryExceptionWrapper::getCause)
								.onErrorReturn(CircuitBreakerOpenException.class, getPostFallbackValue(postId)),
						integration.getPostAvgRanking(postId)
								.onErrorMap(RetryExceptionWrapper.class, RetryExceptionWrapper::getCause)
								.onErrorReturn(CircuitBreakerOpenException.class, getRankingFallbackValue()))
				.doOnError(ex -> LOG.warn("getPost failed: {}", ex)).log();
		Mono<BlogUserDetails> userDetail = postRankingMono.flatMap(post -> integration.getUser(post.getBlogUserId()));
		return Mono.zip(values -> creatPostAggregateWithUser((PostRankingDTO) values[0], (BlogUserDetails) values[1]),
				postRankingMono, userDetail);

	}

	private PostRankingDTO creatPostAggregateWithUser(PostRankingDTO postRankingDTO, BlogUserDetails blogUserDetails) {
		postRankingDTO.setUser(blogUserDetails);
		return postRankingDTO;
	}

	private PostDTO getPostFallbackValue(int postId) {
		LOG.warn("Creating a fallback post for postId = {}", postId);
		return PostDTOBuilder.withDtoBuilder().id(postId).port(serviceUtil.getServicePort()).title("No Results")
				.summary("This is a default message").build();
	}

	private PostRanking getRankingFallbackValue() {
		LOG.warn("Creating a fallback ranking");
		return new PostRanking();
	}

	private PostRankingDTO createPostAggregate(SecurityContext sc, PostDTO post, PostRanking avgRanking) {
		logAuthorizationInfo(sc);
		return PostRankingDTO
			.PostRankingDTOBuilder
			.withPostDto(post)
			.ranking(avgRanking.getAvgRanking())
			.build();
	}

	@Override
	public Mono<Void> createPost(PostDTO body) {
		return ReactiveSecurityContextHolder.getContext().doOnSuccess(sc -> internalCreatePost(sc, body)).then();
	}

	public void internalCreatePost(SecurityContext sc, PostDTO body) {
		try {
			logAuthorizationInfo(sc);
			LOG.debug("internalCreatePost: creates a new for post {}", body.getTitle());
			body.setBlogUserId(Optional.ofNullable(getUserIdFromSC(sc))
					.orElseThrow(() -> new AccessDeniedException(NO_USER_ID)).intValue());
			integration.createPost(body);
			LOG.debug("internalCreatePost: created a new for post {}", body.getTitle());
		} catch (RuntimeException re) {
			throw new InternalApplicationException(re);
		}
	}

	private ResponseEntity<List<PostRankingDTO>> getPostFallbackValue() {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<PostRankingDTO>());
	}

	public Mono<Void> deletePost(int postId) {
		return ReactiveSecurityContextHolder.getContext().doOnSuccess(sc -> internalDeletePost(sc, postId)).then();
	}

	private void internalDeletePost(SecurityContext sc, int postId) {
		try {
			logAuthorizationInfo(sc);
			LOG.debug("internalDeletePost: Deletes a post aggregate for postId: {}", postId);
			integration.deletePost(postId);
			internalDeletePostReviews(postId);
			LOG.debug("internalDeletePost: Deleted a post with postId: {}", postId);
		} catch (RuntimeException re) {
			throw new InternalApplicationException(re);
		}
	}

	private Long getUserIdFromSC(SecurityContext sc) {
		if (sc != null && sc.getAuthentication() != null && sc.getAuthentication() instanceof JwtAuthenticationToken) {
			Jwt jwtToken = ((JwtAuthenticationToken) sc.getAuthentication()).getToken();
			return (Long) jwtToken.getClaims().get("userid");
		} else {
			return null;
		}
	}

	private void logAuthorizationInfo(SecurityContext sc) {
		if (sc != null && sc.getAuthentication() != null && sc.getAuthentication() instanceof JwtAuthenticationToken) {
			Jwt jwtToken = ((JwtAuthenticationToken) sc.getAuthentication()).getToken();
			logAuthorizationInfo(jwtToken);
		} else {
			LOG.warn("No JWT based Authentication supplied, running tests are we?");
		}
	}

	private void logAuthorizationInfo(Jwt jwt) {
		if (jwt == null) {
			LOG.warn("No JWT supplied, running tests are we?");
		} else {
			if (LOG.isDebugEnabled()) {
				URL issuer = jwt.getIssuer();
				List<String> audience = jwt.getAudience();
				Object subject = jwt.getClaims().get("sub");
				Object scopes = jwt.getClaims().get("scope");
				Object expires = jwt.getClaims().get("exp");

				LOG.debug("Authorization info: Subject: {}, scopes: {}, expires {}: issuer: {}, audience: {}", subject,
						scopes, expires, issuer, audience);
			}
		}
	}

	private Throwable handleException(Throwable ex) {

		if (!(ex instanceof WebClientResponseException)) {
			LOG.warn("Got a unexpected error: {}, will rethrow it", ex);
			return ex;
		}

		WebClientResponseException wcre = (WebClientResponseException) ex;

		switch (wcre.getStatusCode()) {

		case NOT_FOUND:
			return new NotFoundException(getErrorMessage(wcre));

		case UNPROCESSABLE_ENTITY:
			return new InvalidInputException(getErrorMessage(wcre));

		default:
			LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
			LOG.warn("Error body: {}", wcre.getResponseBodyAsString());
			return ex;
		}
	}

	private String getErrorMessage(WebClientResponseException ex) {
		try {
			return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
		} catch (IOException ioex) {
			return ex.getMessage();
		}
	}

	@Override
	public Flux<ReviewDTO> getReviews(Long postId) {
		return integration.getReviews(postId);
	}

	@Override
	public Mono<ReviewDTO> getReview(String reviewId) {
		return integration.getReview(reviewId);
	}

	public void internalUpdatePost(SecurityContext sc, PostDTO body) {
		try {
			logAuthorizationInfo(sc);
			LOG.debug("internalUpdatePost: updates an existing postid:{}", body.getId());
			body.setBlogUserId(Optional.ofNullable(getUserIdFromSC(sc))
					.orElseThrow(() -> new AccessDeniedException(NO_USER_ID)).intValue());
			integration.updatePost(body);
			LOG.debug("internalUpdatePost: updated existing postId:{}", body.getId());
		} catch (RuntimeException re) {
			throw new InternalApplicationException(re);
		}
	}

	@Override
	public Mono<PostDTO> updatePost(PostDTO body) {
		return ReactiveSecurityContextHolder.getContext().doOnSuccess(sc -> internalUpdatePost(sc, body))
				.thenReturn(body);
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
			integration.createReview(body);
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
			integration.updateReview(body);
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
			integration.deleteReview(reviewId);
			LOG.debug("internalDeleteReview: Deletes review with reviewId: {}", reviewId);
		} catch (RuntimeException re) {
			throw new InternalApplicationException(re);
		}
	}

	@Override
	public Mono<BlogUserInfoDTO> createNewUser(BlogUserDTO body) {
		return integration.createNewUser(body).onErrorMap(RetryExceptionWrapper.class, RetryExceptionWrapper::getCause)
				.onErrorReturn(CircuitBreakerOpenException.class, getUserFallbackValue());

	}

	private BlogUserInfoDTO getUserFallbackValue() {
		return new BlogUserInfoDTO();
	}

	@Override
	public Mono<BlogUserInfoDTO> updateExistingUser(BlogUserDTO body) {
		return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
				.flatMap(auth -> integration.getUserByEmail(auth.getName())).flatMap(user -> {
					body.setId(user.getId());
					body.setRole(user.getRole());
					body.setEnabled(user.isEnabled());
					return integration.saveUser(body);
				}).onErrorMap(RetryExceptionWrapper.class, RetryExceptionWrapper::getCause)
				.onErrorReturn(CircuitBreakerOpenException.class, getUserFallbackValue());
	}

	@Override
	public Mono<Void> deleteUser(int userId) {
		return integration.deleteUser(userId).onErrorMap(RetryExceptionWrapper.class, RetryExceptionWrapper::getCause);
	}

	@Override
	public Flux<BlogUserInfoDTO> fetchUsers() {
		return integration.getUsers();
	}

	@Override
	public Mono<BlogUserInfoDTO> fetchSignedInUser() {
		return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
				.flatMap(auth -> integration.getUserByEmail(auth.getName()))
				.onErrorMap(RetryExceptionWrapper.class, RetryExceptionWrapper::getCause)
				.onErrorReturn(CircuitBreakerOpenException.class, getUserFallbackValue());

	}*/
}