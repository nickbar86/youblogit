package com.youblog.review.persistence.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
public class Review {
	private ObjectId id;
	@Version
	private Integer version;
	private LocalDateTime datePosted;
	private Integer userId;
	private Long postId;
	private String review;
	private Integer ranking;

	public Review(ObjectId id, LocalDateTime datePosted, Integer userId, Long postId, String review, Integer ranking) {
		super();
		this.id = id;
		this.datePosted = datePosted;
		this.userId = userId;
		this.postId = postId;
		this.review = review;
		this.ranking = ranking;
	}

	public Review() {
		super();
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public LocalDateTime getDatePosted() {
		return datePosted;
	}

	public void setDatePosted(LocalDateTime datePosted) {
		this.datePosted = datePosted;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

}
