package com.youblog.posts.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.youblog.posts.PostParameterResolver;
import com.youblog.posts.persistence.model.Post;
import com.youblog.posts.persistence.repository.PostRepository;

@DataJpaTest(properties = { "eureka.client.enabled=false", "spring.cloud.config.enabled=false" }) // Load application
																									// context. For
																									// spring <2.1 need
// @ExtendWith(SpringExtension.class).
//It does not contain the whole context but a slice to init JPA. Note each test is transactional and 
//and on finish it rolles back everything
@DisplayName("Test Repository Functionality")
@ExtendWith(PostParameterResolver.class)
@ActiveProfiles("test")
public class PostRepositoryTest {

	@Autowired
	PostRepository repo;

	private List<Post> original;

	@BeforeEach
	void init(Map<String, Post> posts) {
		posts.values().forEach(post -> {
			System.out.println("Adding " + post.getTitle() + "  " + post.getId());
			repo.save(post);
		});
		this.original = posts.entrySet().stream().map(Entry::getValue).collect(Collectors.toList());
	}

	@Test
	@DisplayName("Check that fetch works with Pageable info as expected.")
	void givenPageableFetchAllOrderByDateCorrectly() {
		Pageable pageRequest = PageRequest.of(0, 2);
		Page<Post> results = repo.findAllByOrderByDatePostedDesc(pageRequest);
		assertTrue(results.getContent().size() == 2,
				"Fetch should have " + original.size() + " elements in list but got " + results.getContent().size());
		assertTrue(testSameCollections(orderByDateDesc(original), results.getContent()),
				"Result should have Post entities ordered by Date");
	}
	
	@Test
	@DisplayName("Check that fetch works with Null Pageable info as expected.")
	void givenNullPageableFetchAllOrderByDateCorrectly() {
		Page<Post> results = repo.findAllByOrderByDatePostedDesc(null);
		assertTrue(results.getContent().size() == 2,
				"Fetch should have " + original.size() + " elements in list but got " + results.getContent().size());
		assertTrue(testSameCollections(orderByDateDesc(original), results.getContent()),
				"Result should have Post entities ordered by Date");
	}

	private boolean testSameCollections(List<Post> original, List<Post> persisted) {
		boolean result = original.stream().map(Post::getDatePosted).collect(Collectors.toList())
				.equals(persisted.stream().map(Post::getDatePosted).collect(Collectors.toList()));
		return result;
	}

	private List<Post> orderByDateDesc(List<Post> original) {
		return original.stream().sorted(Comparator.comparing(Post::getDatePosted))
				.collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
					Collections.reverse(list);
					return list;
				}));
	}

}
