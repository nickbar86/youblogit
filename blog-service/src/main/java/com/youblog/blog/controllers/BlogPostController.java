package com.youblog.blog.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.handler.advice.RequestHandlerCircuitBreakerAdvice.CircuitBreakerOpenException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.youblog.blog.api.IBlogPost;
import com.youblog.blog.services.PostIntegrationService;
import com.youblog.blog.services.ReviewIntegrationService;
import com.youblog.blog.services.UserIntegrationService;
import com.youblog.blog.services.dto.BlogUserDetails;
import com.youblog.blog.services.dto.PostDTO;
import com.youblog.blog.services.dto.PostDTO.PostDTOBuilder;
import com.youblog.blog.services.dto.PostRanking;
import com.youblog.blog.services.dto.PostRankingDTO;
import com.youblog.blog.services.dto.PostRankingDTO.PostRankingDTOBuilder;
import com.youblog.util.exceptions.InternalApplicationException;
import com.youblog.util.http.ServiceUtil;

import io.github.resilience4j.reactor.retry.RetryExceptionWrapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class BlogPostController extends AbstractConroller implements IBlogPost {

	private static final Logger LOG = LoggerFactory.getLogger(BlogPostController.class);

	private final PostIntegrationService postIntegrationService;
	private final ReviewIntegrationService reviewIntegrationService;
	private final UserIntegrationService userIntegrationService;
	private final ServiceUtil serviceUtil;

	public BlogPostController(PostIntegrationService postIntegrationService,
			ReviewIntegrationService reviewIntegrationService, UserIntegrationService userIntegrationService,
			ServiceUtil serviceUtil) {
		super();
		this.postIntegrationService = postIntegrationService;
		this.reviewIntegrationService = reviewIntegrationService;
		this.userIntegrationService = userIntegrationService;
		this.serviceUtil = serviceUtil;
	}

	private final SecurityContext nullSC = new SecurityContextImpl();

	@Override
	public Mono<ResponseEntity> getPosts(
			@RequestParam(name = "userPosts", required = false, defaultValue = "false") boolean userPosts,
			@RequestParam(name = "page", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(name = "size", required = true, defaultValue = "10") int pageSize,
			@RequestParam(name = "sort", required = true, defaultValue = "id") String sort,
			@RequestParam(name = "direction", required = true, defaultValue = "DESC") Sort.Direction direction) {

		Mono<ClientResponse> postsResponse;
		if (userPosts) {
			postsResponse = ReactiveSecurityContextHolder.getContext().map(this::getUserIdFromSC).flatMap(
					userId -> postIntegrationService.getPostsByUserId(userId, pageIndex, pageSize, sort, direction));
		} else {
			postsResponse = postIntegrationService.getAll(pageIndex, pageSize, sort, direction);
		}
		Flux<PostDTO> posts = postsResponse.flatMapMany(response -> response.bodyToFlux(PostDTO.class).log());
		Flux<PostRanking> avgRankings = posts.flatMap(post -> reviewIntegrationService.getPostAvgRanking(post.getId()));
		Flux<PostRankingDTO> postRankings = avgRankings.collectMap(PostRanking::getPostId, PostRanking::getAvgRanking)
				.flatMapMany(mapToPostRankingDTO(posts));
		return Mono.zip(values -> createPostsAggregate((ClientResponse) values[0], (List<PostRankingDTO>) values[1]),
				postsResponse, postRankings.collectList()).cast(ResponseEntity.class);
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

	private Function<? super Map<Long, Float>, ? extends Publisher<? extends PostRankingDTO>> mapToPostRankingDTO(
			Flux<PostDTO> posts) {
		return rankings -> posts.handle((post, sink) -> {
			PostRankingDTOBuilder builder = PostRankingDTO.PostRankingDTOBuilder.withPostDto(post);
			if (rankings.keySet().contains(post.getId())) {
				sink.next(builder.ranking(rankings.get(post.getId())).build());
			} else {
				sink.next(builder.build());
			}
		});
	}

	@Override
	public Mono<PostRankingDTO> getPost(@PathVariable Integer postId, Integer delay, Integer faultPercent) {
		Mono<PostRankingDTO> postRankingMono = Mono
				.zip(values -> createPostAggregate(
						(SecurityContext) values[0], (PostDTO) values[1], (PostRanking) values[2]),
						ReactiveSecurityContextHolder.getContext().defaultIfEmpty(nullSC),
						postIntegrationService.get(postId)
								.onErrorMap(RetryExceptionWrapper.class, RetryExceptionWrapper::getCause)
								.onErrorReturn(CircuitBreakerOpenException.class, getPostFallbackValue(postId)),
						reviewIntegrationService.getPostAvgRanking(postId)
								.onErrorMap(RetryExceptionWrapper.class, RetryExceptionWrapper::getCause)
								.onErrorReturn(CircuitBreakerOpenException.class, getRankingFallbackValue()))
				.doOnError(ex -> LOG.warn("getPost failed: {}", ex)).log();
		Mono<BlogUserDetails> userDetail = postRankingMono
				.flatMap(post -> userIntegrationService.get(post.getBlogUserId()));
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
		return PostRankingDTO.PostRankingDTOBuilder.withPostDto(post).ranking(avgRanking.getAvgRanking()).build();
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
			postIntegrationService.create(body);
			LOG.debug("internalCreatePost: created a new for post {}", body.getTitle());
		} catch (RuntimeException re) {
			throw new InternalApplicationException(re);
		}
	}

	@Override
	public Mono<PostDTO> updatePost(PostDTO body) {
		return ReactiveSecurityContextHolder.getContext().doOnSuccess(sc -> internalUpdatePost(sc, body))
				.thenReturn(body);
	}

	public void internalUpdatePost(SecurityContext sc, PostDTO body) {
		try {
			logAuthorizationInfo(sc);
			LOG.debug("internalUpdatePost: updates an existing postid:{}", body.getId());
			body.setBlogUserId(Optional.ofNullable(getUserIdFromSC(sc))
					.orElseThrow(() -> new AccessDeniedException(NO_USER_ID)).intValue());
			postIntegrationService.update(body);
			LOG.debug("internalUpdatePost: updated existing postId:{}", body.getId());
		} catch (RuntimeException re) {
			throw new InternalApplicationException(re);
		}
	}

	public Mono<Void> deletePost(int postId) {
		return ReactiveSecurityContextHolder.getContext().doOnSuccess(sc -> internalDeletePost(sc, postId)).then();
	}

	private void internalDeletePost(SecurityContext sc, int postId) {
		try {
			logAuthorizationInfo(sc);
			LOG.debug("internalDeletePost: Deletes a post aggregate for postId: {}", postId);
			postIntegrationService.delete(postId);
			internalDeletePostReviews(postId);
			LOG.debug("internalDeletePost: Deleted a post with postId: {}", postId);
		} catch (RuntimeException re) {
			throw new InternalApplicationException(re);
		}
	}

	private void internalDeletePostReviews(int postId) {
		try {
			LOG.debug("internalDeletePostReviews: Deletes a reviews for postId: {}", postId);
			reviewIntegrationService.deletePostReviews(postId);
			LOG.debug("internalDeleteReview: Deletes review with reviewId: {}", postId);
		} catch (RuntimeException re) {
			throw new InternalApplicationException(re);
		}
	}

}
