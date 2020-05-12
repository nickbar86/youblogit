package com.youblog.blog.services.map;

import java.net.URI;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youblog.blog.services.UserIntegrationService;
import com.youblog.blog.services.dto.BlogUserDTO;
import com.youblog.blog.services.dto.BlogUserDetails;
import com.youblog.blog.services.dto.BlogUserInfoDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserIntegrationServiceImpl extends AbstractIntegrationService implements UserIntegrationService {
	private static final Logger LOG = LoggerFactory.getLogger(UserIntegrationServiceImpl.class);

	private static final String USER_SERVICE = "http://user-service";
	private WebClient webClient;
	private final int userServiceTimeoutSec;

	public UserIntegrationServiceImpl(ObjectMapper mapper, @Qualifier("authenticatedWebClient") WebClient webClient,
			@Value("${app.user-service.timeoutSec}") int userServiceTimeoutSec) {
		super(mapper);
		this.webClient = webClient;
		this.userServiceTimeoutSec = userServiceTimeoutSec;
	}

	@Override
	@Retry(name = "user-service")
	@CircuitBreaker(name = "user-service")
	public Mono<BlogUserDetails> get(Integer id) {
		URI url = UriComponentsBuilder.fromUriString(USER_SERVICE + "/blogUsers/{userId}").build(id);
		LOG.debug("Will call the getUserDetails API on URL: {}", url);
		return webClient.get().uri(url).retrieve().bodyToMono(BlogUserDetails.class).log()
				.onErrorMap(WebClientResponseException.class, this::handleException)
				.timeout(Duration.ofSeconds(userServiceTimeoutSec));
	}

	@Override
	@CircuitBreaker(name = "user-service")
	public Mono<BlogUserInfoDTO> create(BlogUserDTO body) {
		UriComponents url = UriComponentsBuilder.fromUriString(USER_SERVICE + "/blogUsers").build();
		LOG.debug("Will call the create User API on URL: {}", url);
		body.setRole("USER");
		body.setEnabled(true);
		return webClient.post().uri(url.toUri()).contentType(MediaType.APPLICATION_JSON).bodyValue(body).retrieve()
				.bodyToMono(BlogUserInfoDTO.class).log().onErrorMap(WebClientResponseException.class, this::handleException)
				.timeout(Duration.ofSeconds(userServiceTimeoutSec));
	}

	@Override
	@CircuitBreaker(name = "user-service")
	public Mono<Void> deleteUser(int userId) {
		URI url = UriComponentsBuilder.fromUriString(USER_SERVICE + "/blogUsers/{userId}").build(userId);
		LOG.debug("Will call the delete User API on URL: {}", url);
		return webClient.delete().uri(url.getPath()).exchange()
				.onErrorMap(WebClientResponseException.class, this::handleException)
				.timeout(Duration.ofSeconds(userServiceTimeoutSec)).then();
	}

	@Override
	@Retry(name = "user-service")
	@CircuitBreaker(name = "user-service")
	public Flux<BlogUserInfoDTO> getAll() {
		UriComponents url = UriComponentsBuilder.fromUriString(USER_SERVICE + "/blogUsers").build();
		LOG.debug("Will call the create User API on URL: {}", url);
		return webClient.get().uri(url.toUri()).retrieve().bodyToFlux(BlogUserInfoDTO.class).log()
				.onErrorMap(WebClientResponseException.class, this::handleException)
				.timeout(Duration.ofSeconds(userServiceTimeoutSec));
	}

	@Override
	@Retry(name = "user-service")
	@CircuitBreaker(name = "user-service")
	public Mono<BlogUserInfoDTO> update(BlogUserDTO body) {
		UriComponents url = UriComponentsBuilder.fromUriString(USER_SERVICE + "/blogUsers").build();
		LOG.debug("Will call the update User API on URL: {}", url.toUri());
		return webClient.post().uri(url.toUri()).contentType(MediaType.APPLICATION_JSON).bodyValue(body).retrieve()
				.bodyToMono(BlogUserInfoDTO.class).log()
				.onErrorMap(WebClientResponseException.class, this::handleException)
				.timeout(Duration.ofSeconds(userServiceTimeoutSec));
	}

	@Retry(name = "user-service")
	@CircuitBreaker(name = "user-service")
	public Mono<BlogUserInfoDTO> getUserByEmail(String email) {
		UriComponents url = UriComponentsBuilder
				.fromUriString(USER_SERVICE + "/blogUsers/search/findByEmailIgnoreCase").queryParam("email", email)
				.build();
		LOG.debug("Will call the getUserDetails by email API on URL: {}", url.toUri());
		return webClient.get().uri(url.toUri()).retrieve().bodyToMono(BlogUserInfoDTO.class).log()
				.onErrorMap(WebClientResponseException.class, this::handleException)
				.timeout(Duration.ofSeconds(userServiceTimeoutSec));
	}

}
