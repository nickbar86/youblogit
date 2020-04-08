package com.youblog.posts;

import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import com.youblog.posts.persistence.model.Post;

public class PostParameterResolver implements ParameterResolver {

	private final Map<String, Post> posts;

	public PostParameterResolver() {
		Map<String, Post> posts = new HashMap<>();
		Post entity = new Post();
		entity.setContent("This is a test 1");
		entity.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 1));
		entity.setDateUpdated(LocalDateTime.of(2020, 1, 1, 1, 1));
		entity.setBlogUserId(1);
		entity.setPort(123);
		entity.setSummary("Test Summary 1");
		entity.setTitle("Test 1");
		//entity.setId(1L);
		entity.setVersion(0);
		posts.put("Test 1", entity);
		
		entity = new Post();
		entity.setContent("This is a test 2");
		entity.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 2));
		entity.setDateUpdated(LocalDateTime.of(2020, 1, 1, 1, 2));
		entity.setBlogUserId(2);
		entity.setPort(123);
		entity.setSummary("Test Summary");
		entity.setTitle("Test 2");
		//entity.setId(2L);
		entity.setVersion(0);
		posts.put("Test 2", entity);
		
		entity = new Post();
		entity.setContent("This is a test 3");
		entity.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 3));
		entity.setDateUpdated(LocalDateTime.of(2020, 1, 1, 1, 3));
		entity.setBlogUserId(3);
		entity.setPort(123);
		entity.setSummary("Test Summary");
		entity.setTitle("Test 3");
		//entity.setId(3L);
		entity.setVersion(0);
		posts.put("Test 3", entity);
		
		entity = new Post();
		entity.setContent("This is a test 4");
		entity.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 4));
		entity.setDateUpdated(LocalDateTime.of(2020, 1, 1, 1, 4));
		entity.setBlogUserId(4);
		entity.setPort(123);
		entity.setSummary("Test Summary");
		entity.setTitle("Test 4");
		//entity.setId(4L);
		entity.setVersion(0);
		posts.put("Test 4", entity);
		
		entity = new Post();
		entity.setContent("This is a test 5");
		entity.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 5));
		entity.setDateUpdated(LocalDateTime.of(2020, 1, 1, 1, 5));
		entity.setBlogUserId(5);
		entity.setPort(123);
		entity.setSummary("Test Summary");
		entity.setTitle("Test 5");
		//entity.setId(5L);
		entity.setVersion(0);
		posts.put("Test 5", entity);
		
		entity = new Post();
		entity.setContent("This is a test 6");
		entity.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 6));
		entity.setDateUpdated(LocalDateTime.of(2020, 1, 1, 1, 6));
		entity.setBlogUserId(6);
		entity.setPort(123);
		entity.setSummary("Test Summary");
		entity.setTitle("Test 6");
		entity.setId(6L);
		entity.setVersion(0);
		posts.put("Test 6", entity);
		
		entity = new Post();
		entity.setContent("This is a test 7");
		entity.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 7));
		entity.setDateUpdated(LocalDateTime.of(2020, 1, 1, 1, 7));
		entity.setBlogUserId(7);
		entity.setPort(123);
		entity.setSummary("Test Summary");
		entity.setTitle("Test 7");
		//entity.setId(7L);
		entity.setVersion(0);
		posts.put("Test 7", entity);
		
		entity = new Post();
		entity.setContent("This is a test 8");
		entity.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 8));
		entity.setDateUpdated(LocalDateTime.of(2020, 1, 1, 1, 8));
		entity.setBlogUserId(8);
		entity.setPort(123);
		entity.setSummary("Test Summary");
		entity.setTitle("Test 8");
		//entity.setId(8L);
		entity.setVersion(0);
		posts.put("Test 8", entity);
		
		entity = new Post();
		entity.setContent("This is a test 9");
		entity.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 9));
		entity.setDateUpdated(LocalDateTime.of(2020, 1, 1, 1, 9));
		entity.setBlogUserId(9);
		entity.setPort(123);
		entity.setSummary("Test Summary");
		entity.setTitle("Test 9");
		//entity.setId(9L);
		entity.setVersion(0);
		posts.put("Test 9", entity);
		
		entity = new Post();
		entity.setContent("This is a test 10");
		entity.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 10));
		entity.setDateUpdated(LocalDateTime.of(2020, 1, 1, 1, 10));
		entity.setBlogUserId(10);
		entity.setPort(123);
		entity.setSummary("Test Summary");
		entity.setTitle("Test 10");
		//entity.setId(10L);
		entity.setVersion(0);
		posts.put("Test 10", entity);
		
		entity = new Post();
		entity.setContent("This is a test 11");
		entity.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 11));
		entity.setDateUpdated(LocalDateTime.of(2020, 1, 1, 1, 11));
		entity.setBlogUserId(11);
		entity.setPort(123);
		entity.setSummary("Test Summary");
		entity.setTitle("Test 11");
		//entity.setId(11L);
		entity.setVersion(0);
		posts.put("Test 11", entity);
		
		this.posts = posts;
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		Parameter parameter = parameterContext.getParameter();
		return Objects.equals(parameter.getParameterizedType().getTypeName(),
				"java.util.Map<java.lang.String, com.youblog.posts.persistence.model.Post>");
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return posts;
	}

}
