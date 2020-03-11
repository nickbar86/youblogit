package com.youblog.review.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.youblog.review.persistence.model.Review;
import com.youblog.review.service.dto.ReviewDTO;

public class ReviewMapperTest {
	private ReviewMapper mapper = Mappers.getMapper(ReviewMapper.class);

	@Test
	public void mapperTests() {

		assertNotNull(mapper);

		ReviewDTO api = new ReviewDTO(null, LocalDateTime.of(2020, 2, 1, 1, 1), 1, 2L, "ok", 5);

		Review entity = mapper.apiToEntity(api);

		assertEquals(api.getDatePosted(), entity.getDatePosted());
		assertEquals(api.getPostId(), entity.getPostId());
		assertEquals(api.getRanking(), entity.getRanking());
		assertEquals(api.getReview(), entity.getReview());
		assertEquals(api.getUserId(), entity.getUserId());

		ReviewDTO api2 = mapper.entityToApi(entity);

		assertEquals(api2.getDatePosted(), entity.getDatePosted());
		assertEquals(api2.getPostId(), entity.getPostId());
		assertEquals(api2.getRanking(), entity.getRanking());
		assertEquals(api2.getReview(), entity.getReview());
		assertEquals(api2.getUserId(), entity.getUserId());
	}
}
