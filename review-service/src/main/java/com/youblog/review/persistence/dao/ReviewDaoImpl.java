package com.youblog.review.persistence.dao;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import com.youblog.review.persistence.model.Review;
import com.youblog.review.service.dto.PostRanking;

import reactor.core.publisher.Mono;

public class ReviewDaoImpl implements ReviewDao {
	@Autowired
	ReactiveMongoTemplate mongoTemplate;

	private MatchOperation getMatchOperation(Long postId) {
		Criteria postCriteria = Criteria.where("postId").is(postId);
		return match(postCriteria);
	}

	private GroupOperation getGroupOperation() {
		return group("postId").avg("ranking").as("avgRanking");
	}

	private ProjectionOperation getProjectOperation() {
		return project("_id.postId", "avgRanking");
	}
	GroupOperation groupByStateAndSumPop = group("postId")
			  .sum("ranking").as("avgRanking");
	public Mono<PostRanking> getPostAvgRanking(Long postId) {
		MatchOperation matchOperation = getMatchOperation(postId);
		GroupOperation groupOperation = getGroupOperation();
		ProjectionOperation projectionOperation = getProjectOperation();

		return mongoTemplate.aggregate(Aggregation.newAggregation(matchOperation,groupOperation,projectionOperation),
				Review.class, PostRanking.class).next();
	}

}
