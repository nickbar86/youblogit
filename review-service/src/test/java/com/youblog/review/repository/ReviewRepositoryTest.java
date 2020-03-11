package com.youblog.review.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import com.youblog.review.persistence.model.Review;
import com.youblog.review.persistence.repository.ReviewRepository;
import com.youblog.review.service.dto.PostRanking;
import com.youblog.util.exceptions.NotFoundException;

import reactor.test.StepVerifier;

@DisplayName("Test Review Repository Functionality")
@DataMongoTest(properties = { "eureka.client.enabled=false", "spring.cloud.config.enabled=false" })
@ActiveProfiles("test")
public class ReviewRepositoryTest {
	private static final Logger LOG = LoggerFactory.getLogger(ReviewRepositoryTest.class);

	@Autowired
	private ReviewRepository repository;

	@BeforeEach
	public void setupDb() {
		repository.deleteAll().block();
	}

	@Test
	public void create() {
		Review reviewEntity = new Review(null, LocalDateTime.of(2020, 1, 1, 1, 4), 2, 2L, "Test", 4);
		StepVerifier.create(repository.save(reviewEntity))
				.expectNextMatches(createdEntity -> reviewEntity.getId().equals(createdEntity.getId()))
				.verifyComplete();
		StepVerifier.create(repository.findById(reviewEntity.getId().toHexString()))
				.expectNextMatches(foundEntity -> areReviewsEqual(reviewEntity, foundEntity)).verifyComplete();
		StepVerifier.create(repository.count()).expectNext(1l).verifyComplete();
	}

	@Test
	public void update() {
		Review savedEntity = createEntity();
		savedEntity.setReview("Test Update");
		StepVerifier.create(repository.save(savedEntity))
				.expectNextMatches(updatedEntity -> updatedEntity.getReview().equals("Test Update")).verifyComplete();
		StepVerifier.create(repository.findById(savedEntity.getId().toHexString()))
				.expectNextMatches(
						foundEntity -> foundEntity.getVersion() == 1 && foundEntity.getReview().equals("Test Update"))
				.verifyComplete();
		repository.deleteAll().block();
	}

	@Test
	public void delete() {
		Review reviewEntity = createEntity();
		StepVerifier.create(repository.delete(reviewEntity)).verifyComplete();
		StepVerifier.create(repository.findById(reviewEntity.getId().toHexString()))
				.expectError(NotFoundException.class);
		repository.deleteAll().block();
	}

	@Test
	public void getByPostId() {
		Review reviewEntity = createEntity();
		StepVerifier.create(repository.findByPostId(reviewEntity.getPostId()))
				.expectNextMatches(foundEntity -> areReviewsEqual(reviewEntity, foundEntity)).verifyComplete();
	}

	@Test
	public void optimisticLockError() {
		Review savedEntity = createEntity();
		// Store the saved entity in two separate entity objects
		Review entity1 = repository.findById(savedEntity.getId().toHexString()).block();
		Review entity2 = repository.findById(savedEntity.getId().toHexString()).block();

		// Update the entity using the first entity object
		entity1.setReview("n1");
		repository.save(entity1).block();

		// Update the entity using the second entity object.
		// This should fail since the second entity now holds a old version number, i.e.
		// a Optimistic Lock Error
		StepVerifier.create(repository.save(entity2)).expectError(OptimisticLockingFailureException.class).verify();

		// Get the updated entity from the database and verify its new sate
		StepVerifier.create(repository.findById(savedEntity.getId().toHexString()))
				.expectNextMatches(foundEntity -> foundEntity.getVersion() == 1 && foundEntity.getReview().equals("n1"))
				.verifyComplete();
	}

	@Test
	public void givenPostIdTestAvgRankingValue() {
		createEntity();
		PostRanking rank = repository.getPostAvgRanking(2L).block();
		assertTrue(rank.getAvgRanking() == 4);

	}

	@Test
	public void givenNonExistentPostIdTestAvgRankingError() {
		StepVerifier.create(repository.getPostAvgRanking(2L)).expectError(NullPointerException.class);
	}

	private Review createEntity() {
		Review reviewEntity = new Review(null, LocalDateTime.of(2020, 1, 1, 1, 4), 2, 2L, "Test", 4);
		return repository.save(reviewEntity).block();
	}

	private boolean areReviewsEqual(Review expectedEntity, Review actualEntity) {
		return (expectedEntity.getVersion() == actualEntity.getVersion())
				&& (expectedEntity.getPostId() == actualEntity.getPostId())
				&& (expectedEntity.getReview().equals(actualEntity.getReview()))
				&& (expectedEntity.getDatePosted().equals(actualEntity.getDatePosted()))
				&& (expectedEntity.getUserId() == actualEntity.getUserId());
	}
}
