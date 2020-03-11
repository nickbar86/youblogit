package com.youblog.review;

import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import com.youblog.review.persistence.model.Review;

public class ReviewParameterResolver implements ParameterResolver {

	private final Map<String, Review> reviews;

	public ReviewParameterResolver() {
		Map<String, Review> reviews = new HashMap<>();
		Review entity = new Review();
		entity.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 1));
		entity.setPostId(2L);
		entity.setRanking(5);
		entity.setReview("1");
		entity.setUserId(22);
		entity.setId(new ObjectId());
		reviews.put("1", entity);
		entity = new Review();
		entity.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 2));
		entity.setPostId(2L);
		entity.setRanking(5);
		entity.setReview("2");
		entity.setUserId(22);
		entity.setId(new ObjectId());
		reviews.put("2", entity);
		entity = new Review();
		entity.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 3));
		entity.setPostId(2L);
		entity.setRanking(5);
		entity.setReview("3");
		entity.setUserId(22);
		entity.setId(new ObjectId());
		reviews.put("3", entity);
		this.reviews = reviews;
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		Parameter parameter = parameterContext.getParameter();
		return Objects.equals(parameter.getParameterizedType().getTypeName(),
				"java.util.Map<java.lang.String, com.youblog.review.persistence.model.Review>");
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return reviews;
	}

}
