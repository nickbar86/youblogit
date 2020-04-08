package com.youblog.blog.services;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youblog.blog.services.dto.BlogUserDTO;
import com.youblog.blog.services.dto.BlogUserDetails;
import com.youblog.blog.services.dto.BlogUserInfoDTO;
import com.youblog.blog.services.dto.PostDTO;
import com.youblog.blog.services.dto.PostRanking;
import com.youblog.blog.services.dto.ReviewDTO;
import com.youblog.util.Event;
import com.youblog.util.Event.Type;
import com.youblog.util.exceptions.InvalidInputException;
import com.youblog.util.exceptions.NotFoundException;
import com.youblog.util.http.HttpErrorInfo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@EnableBinding(BlogPostsIntegration.MessageSources.class)
@Component
public class BlogPostsIntegration {

	private static final Logger LOG = LoggerFactory.getLogger(BlogPostsIntegration.class);

	private final String postServiceUrl = "http://post-service";
	private final String reviewServiceUrl = "http://review-service";
	private final String userServiceUrl = "http://user-service";

	private final ObjectMapper mapper;
	private final WebClient.Builder webClientBuilder;

	private WebClient webClient;
	private WebClient authenticatedWebClient;

	private final MessageSources messageSources;

	private final int postServiceTimeoutSec;
	private final int reviewServiceTimeoutSec;
	private final int userServiceTimeoutSec;

	public interface MessageSources {

		String OUTPUT_POSTS = "output-posts";
		String OUTPUT_REVIEWS = "output-reviews";

		@Output(OUTPUT_POSTS)
		MessageChannel outputPosts();

		@Output(OUTPUT_REVIEWS)
		MessageChannel outputReviews();
	}

	@Autowired
	public BlogPostsIntegration(WebClient.Builder webClientBuilder, ObjectMapper mapper, MessageSources messageSources,
			@Value("${app.post-service.timeoutSec}") int postServiceTimeoutSec,
			@Value("${app.review-service.timeoutSec}") int reviewServiceTimeoutSec,
			@Value("${app.user-service.timeoutSec}") int userServiceTimeoutSec

	) {
		this.webClientBuilder = webClientBuilder;
		this.mapper = mapper;
		this.messageSources = messageSources;
		this.postServiceTimeoutSec = postServiceTimeoutSec;
		this.reviewServiceTimeoutSec = reviewServiceTimeoutSec;
		this.userServiceTimeoutSec = userServiceTimeoutSec;
	}

	public PostDTO createPost(PostDTO body) {
		messageSources.outputPosts()
				.send(MessageBuilder.withPayload(new Event(Type.CREATE, body.getTitle(), body)).build());
		return body;
	}

	@Retry(name = "post-service")
	@CircuitBreaker(name = "post-service")
	public Mono<PostDTO> getPost(Integer postId, Integer delay, Integer faultPercent) {

		URI url = UriComponentsBuilder
				.fromUriString(postServiceUrl + "/posts/{postId}?delay={delay}&faultPercent={faultPercent}")
				.build(postId, delay, faultPercent);
		LOG.debug("Will call the getPost API on URL: {}", url);

		return getWebClient().get().uri(url).retrieve().bodyToMono(PostDTO.class).log()
				.onErrorMap(WebClientResponseException.class, ex -> handleException(ex))
				.timeout(Duration.ofSeconds(postServiceTimeoutSec));
	}

	@Retry(name = "post-service")
	@CircuitBreaker(name = "post-service")
	public Mono<ClientResponse> getPosts(int pageIndex, int pageSize, String sort, Direction direction) {
		UriComponents url = UriComponentsBuilder.fromUriString(postServiceUrl + "/posts/").queryParam("page", pageIndex)
				.queryParam("size", pageSize).queryParam("sort", sort).queryParam("direction", direction).build();
		LOG.debug("Will call the getPost API on URL: {}", url);
		return getWebClient().get().uri(url.toUri()).exchange().log()
				.onErrorMap(WebClientResponseException.class, ex -> handleException(ex))
				.timeout(Duration.ofSeconds(postServiceTimeoutSec));
	}

	public void deletePost(int postId) {
		messageSources.outputPosts().send(MessageBuilder.withPayload(new Event(Type.DELETE, postId, null)).build());
	}

	@Retry(name = "review-service")
	@CircuitBreaker(name = "review-service")
	public Flux<ReviewDTO> getReviews(long postId) {
		URI url = UriComponentsBuilder.fromUriString(reviewServiceUrl + "/reviews/?postId={postId}").build(postId);
		LOG.debug("Will call the getReviews API on URL: {}", url);
		return getWebClient().get().uri(url).retrieve().bodyToFlux(ReviewDTO.class).log()
				.onErrorMap(WebClientResponseException.class, ex -> handleException(ex))
				.timeout(Duration.ofSeconds(reviewServiceTimeoutSec));
	}

	@Retry(name = "review-service")
	@CircuitBreaker(name = "review-service")
	public Mono<PostRanking> getPostAvgRanking(long postId) {
		URI url = UriComponentsBuilder.fromUriString(reviewServiceUrl + "/reviews/posts/{postId}/rankings/avg")
				.buildAndExpand(postId).toUri();
		LOG.debug("Will call the getReviews API on URL: {}", url);
		return getWebClient().get().uri(url).retrieve().bodyToMono(PostRanking.class).log()
				.onErrorMap(WebClientResponseException.class, ex -> handleException(ex))
				.timeout(Duration.ofSeconds(reviewServiceTimeoutSec));
	}

	private WebClient getWebClient() {
		if (webClient == null) {
			webClient = webClientBuilder.build();
		}
		return webClient;
	}
	
	private WebClient getAuthenticatedClient() {
		if (authenticatedWebClient==null) {
			authenticatedWebClient  = webClientBuilder.filter(ExchangeFilterFunctions.basicAuthentication("nick@example.com", "secret")).build();
		}
		return authenticatedWebClient;
	}

	private Throwable handleException(Throwable ex) {

		if (!(ex instanceof WebClientResponseException)) {
			LOG.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
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

	@Retry(name = "review-service")
	@CircuitBreaker(name = "review-service")
	public Mono<ReviewDTO> getReview(String reviewId) {
		URI url = UriComponentsBuilder.fromUriString(reviewServiceUrl + "/reviews/{reviewId}/").build(reviewId);
		LOG.debug("Will call the getReviews API on URL: {}", url);
		return getWebClient().get().uri(url).retrieve().bodyToMono(ReviewDTO.class).log()
				.onErrorMap(WebClientResponseException.class, ex -> handleException(ex))
				.timeout(Duration.ofSeconds(reviewServiceTimeoutSec));
	}

	public PostDTO updatePost(PostDTO body) {
		messageSources.outputPosts()
				.send(MessageBuilder.withPayload(new Event(Type.UPDATE, body.getId(), body)).build());
		return body;
	}

	public void createReview(ReviewDTO body) {
		messageSources.outputReviews()
				.send(MessageBuilder.withPayload(new Event(Type.CREATE, body.getUserId(), body)).build());
	}

	public ReviewDTO updateReview(ReviewDTO body) {
		messageSources.outputReviews()
				.send(MessageBuilder.withPayload(new Event(Type.UPDATE, body.getReviewId(), body)).build());
		return body;
	}

	public void deleteReview(int reviewId) {
		messageSources.outputReviews().send(MessageBuilder.withPayload(new Event(Type.DELETE, reviewId, null)).build());
	}

	public void deletePostReviews(int postId) {
		messageSources.outputReviews()
				.send(MessageBuilder.withPayload(new Event(Type.DELETE, "postId:" + postId, null)).build());
	}

	@CircuitBreaker(name = "user-service")
	public Mono<BlogUserInfoDTO> createNewUser(BlogUserDTO body) {
		UriComponents url = UriComponentsBuilder.fromUriString(userServiceUrl + "/users/").build();
		LOG.debug("Will call the create User API on URL: {}", url);
		return getAuthenticatedClient().put().uri(url.getPath()).contentType(MediaType.APPLICATION_JSON).bodyValue(body)
				.retrieve().bodyToMono(BlogUserInfoDTO.class).log()
				.onErrorMap(WebClientResponseException.class, ex -> handleException(ex))
				.timeout(Duration.ofSeconds(userServiceTimeoutSec));
	}

	@Retry(name = "user-service")
	@CircuitBreaker(name = "user-service")
	public Mono<BlogUserInfoDTO> updateExistingUser(BlogUserDTO body) {
		UriComponents url = UriComponentsBuilder.fromUriString(userServiceUrl + "/users/").build();
		LOG.debug("Will call the update User API on URL: {}", url);
		return getAuthenticatedClient().post().uri(url.getPath()).contentType(MediaType.APPLICATION_JSON).bodyValue(body)
				.retrieve().bodyToMono(BlogUserInfoDTO.class).log()
				.onErrorMap(WebClientResponseException.class, ex -> handleException(ex))
				.timeout(Duration.ofSeconds(reviewServiceTimeoutSec));
	}

	@CircuitBreaker(name = "user-service")
	public Flux<BlogUserInfoDTO> getUsers() {
		UriComponents url = UriComponentsBuilder.fromUriString(userServiceUrl + "/users/").build();
		LOG.debug("Will call the create User API on URL: {}", url);
		return getAuthenticatedClient().get().uri(url.getPath()).retrieve().bodyToFlux(BlogUserInfoDTO.class).log()
				.onErrorMap(WebClientResponseException.class, ex -> handleException(ex))
				.timeout(Duration.ofSeconds(reviewServiceTimeoutSec));
	}

	@CircuitBreaker(name = "user-service")
	public Mono<Void> deleteUser(int userId) {
		URI url = UriComponentsBuilder.fromUriString(userServiceUrl + "/users/{userId}").build(userId);
		LOG.debug("Will call the delete User API on URL: {}", url);
		return getAuthenticatedClient().delete().uri(url.getPath()).exchange()
				.onErrorMap(WebClientResponseException.class, ex -> handleException(ex))
				.timeout(Duration.ofSeconds(reviewServiceTimeoutSec)).then();
	}

	@Retry(name = "user-service")
	@CircuitBreaker(name = "user-service")
	public Mono<BlogUserDetails> getUser(Integer userId) {
		URI url = UriComponentsBuilder.fromUriString(userServiceUrl + "/blogUsers/{userId}").build(userId);
		LOG.debug("Will call the getUserDetails API on URL: {}", url);
		return getAuthenticatedClient().get().uri(url).retrieve().bodyToMono(BlogUserDetails.class).log()
				.onErrorMap(WebClientResponseException.class, ex -> handleException(ex))
				.timeout(Duration.ofSeconds(reviewServiceTimeoutSec));
	}
}