package com.youblog.blog.services.map;

import java.net.URI;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youblog.blog.services.BlogPostsIntegration;
import com.youblog.blog.services.ReviewIntegrationService;
import com.youblog.blog.services.BlogPostsIntegration.MessageSources;
import com.youblog.blog.services.dto.PostRanking;
import com.youblog.blog.services.dto.ReviewDTO;
import com.youblog.util.Event;
import com.youblog.util.Event.Type;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@EnableBinding(MessageSources.class)
public class ReviewIntegrationServiceImpl extends AbstractIntegrationService implements ReviewIntegrationService {
	private static final Logger LOG = LoggerFactory.getLogger(ReviewIntegrationServiceImpl.class);

	private static final String REVIEW_SERVICE = "http://review-service";
	private WebClient webClient;
	private final MessageSources messageSources;
	private final int reviewServiceTimeoutSec;

	public ReviewIntegrationServiceImpl(ObjectMapper mapper, MessageSources messageSources,
			@Value("${app.review-service.timeoutSec}") int reviewServiceTimeoutSec,
			@Qualifier("simpleWebClient") WebClient webClient) {
		super(mapper);
		this.webClient = webClient;
		this.messageSources = messageSources;
		this.reviewServiceTimeoutSec = reviewServiceTimeoutSec;
	}

	@Override
	@Retry(name = "review-service")
	@CircuitBreaker(name = "review-service")
	public Mono<ReviewDTO> get(Integer id) {
		URI url = UriComponentsBuilder.fromUriString(REVIEW_SERVICE + "/reviews/{reviewId}/").build(id);
		LOG.debug("Will call the getReviews API on URL: {}", url);
		return webClient.get().uri(url).retrieve().bodyToMono(ReviewDTO.class).log()
				.onErrorMap(WebClientResponseException.class, this::handleException)
				.timeout(Duration.ofSeconds(reviewServiceTimeoutSec));
	}

	@Override
	public Mono<Void> create(ReviewDTO body) {
		messageSources.outputReviews()
				.send(MessageBuilder.withPayload(new Event(Type.CREATE, body.getUserId(), body)).build());
		return Mono.empty();
	}

	@Override
	public void delete(int id) {
		messageSources.outputReviews().send(MessageBuilder.withPayload(new Event(Type.DELETE, id, null)).build());
	}

	@Override
	@Retry(name = "review-service")
	@CircuitBreaker(name = "review-service")
	public Flux<ReviewDTO> getAll(long postId) {
		URI url = UriComponentsBuilder.fromUriString(REVIEW_SERVICE + "/reviews/?postId={postId}").build(postId);
		LOG.debug("Will call the getReviews API on URL: {}", url);
		return webClient.get().uri(url).retrieve().bodyToFlux(ReviewDTO.class).log()
				.onErrorMap(WebClientResponseException.class, this::handleException)
				.timeout(Duration.ofSeconds(reviewServiceTimeoutSec));
	}

	@Override
	@Retry(name = "review-service")
	@CircuitBreaker(name = "review-service")
	public Mono<PostRanking> getPostAvgRanking(long postId) {
		URI url = UriComponentsBuilder.fromUriString(REVIEW_SERVICE + "/reviews/posts/{postId}/rankings/avg")
				.buildAndExpand(postId).toUri();
		LOG.debug("Will call the getReviews API on URL: {}", url);
		return webClient.get().uri(url).retrieve().bodyToMono(PostRanking.class).log()
				.onErrorMap(WebClientResponseException.class, this::handleException)
				.timeout(Duration.ofSeconds(reviewServiceTimeoutSec));
	}

	@Override
	public void deletePostReviews(int postId) {
		messageSources.outputReviews()
				.send(MessageBuilder.withPayload(new Event(Type.DELETE, "postId:" + postId, null)).build());
	}

	@Override
	public ReviewDTO update(ReviewDTO body) {
		messageSources.outputReviews()
				.send(MessageBuilder.withPayload(new Event(Type.UPDATE, body.getReviewId(), body)).build());
		return body;
	}

}
