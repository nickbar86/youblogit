package com.youblog.blog.services.map;

import java.net.URI;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youblog.blog.services.BlogPostsIntegration;
import com.youblog.blog.services.PostIntegrationService;
import com.youblog.blog.services.BlogPostsIntegration.MessageSources;
import com.youblog.blog.services.dto.PostDTO;
import com.youblog.util.Event;
import com.youblog.util.Event.Type;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import reactor.core.publisher.Mono;

@Service
@EnableBinding(MessageSources.class)
public class PostIntegrationServiceImpl extends AbstractIntegrationService implements PostIntegrationService {
	private static final Logger LOG = LoggerFactory.getLogger(PostIntegrationServiceImpl.class);

	private static final String POST_SERVICE = "http://post-service";
	private final WebClient webClient;
	private final MessageSources messageSources;
	private final int postServiceTimeoutSec;

	public PostIntegrationServiceImpl(ObjectMapper mapper, @Qualifier("simpleWebClient") WebClient webClient,
			MessageSources messageSources, @Value("${app.post-service.timeoutSec}") int postServiceTimeoutSec) {
		super(mapper);
		this.webClient = webClient;
		this.messageSources = messageSources;
		this.postServiceTimeoutSec = postServiceTimeoutSec;
	}

	@Override
	@Retry(name = "post-service")
	@CircuitBreaker(name = "post-service")
	public Mono<PostDTO> get(Integer postId) {
		URI url = UriComponentsBuilder
				.fromUriString(POST_SERVICE + "/posts/{postId}?delay={delay}&faultPercent={faultPercent}")
				.build(postId);
		LOG.debug("Will call the getPost API on URL: {}", url);

		return webClient.get().uri(url).retrieve().bodyToMono(PostDTO.class).log()
				.onErrorMap(WebClientResponseException.class, this::handleException)
				.timeout(Duration.ofSeconds(postServiceTimeoutSec));
	}

	@Override
	public Mono<Void> create(PostDTO body) {
		messageSources.outputPosts()
				.send(MessageBuilder.withPayload(new Event(Type.CREATE, body.getTitle(), body)).build());
		return Mono.empty();
	}

	@Retry(name = "post-service")
	@CircuitBreaker(name = "post-service")
	public Mono<ClientResponse> getAll(int pageIndex, int pageSize, String sort, Direction direction) {
		UriComponents url = UriComponentsBuilder.fromUriString(POST_SERVICE + "/posts/").queryParam("page", pageIndex)
				.queryParam("size", pageSize).queryParam("sort", sort).queryParam("direction", direction).build();
		LOG.debug("Will call the getPost API on URL: {}", url);
		return webClient.get().uri(url.toUri()).exchange().log()
				.onErrorMap(WebClientResponseException.class, this::handleException)
				.timeout(Duration.ofSeconds(postServiceTimeoutSec));
	}

	@Override
	public void delete(int postId) {
		messageSources.outputPosts().send(MessageBuilder.withPayload(new Event(Type.DELETE, postId, null)).build());
	}

	@Override
	public PostDTO update(PostDTO body) {
		messageSources.outputPosts()
				.send(MessageBuilder.withPayload(new Event(Type.UPDATE, body.getId(), body)).build());
		return body;
	}

	@Override
	public Mono<ClientResponse> getPostsByUserId(Long id, int pageIndex, int pageSize, String sort,
			Direction direction) {
		UriComponents url = UriComponentsBuilder
				.fromUriString(POST_SERVICE + "/posts/")
				.queryParam("blogUserId", id)
				.queryParam("page", pageIndex)
				.queryParam("size", pageSize)
				.queryParam("sort", sort)
				.queryParam("direction", direction).build();
		LOG.debug("Will call the getPost API on URL: {}", url);
		return webClient.get().uri(url.toUri()).exchange().log()
				.onErrorMap(WebClientResponseException.class, this::handleException)
				.timeout(Duration.ofSeconds(postServiceTimeoutSec));
	}

}
