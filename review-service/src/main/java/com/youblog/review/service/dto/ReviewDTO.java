package com.youblog.review.service.dto;

import java.time.LocalDateTime;

public class ReviewDTO {
	private String reviewId;
	private LocalDateTime datePosted;
	private Integer userId;
	private Long postId;
	private String review;
	private Integer ranking;

	public ReviewDTO() {
		super();
	}


	public ReviewDTO(String reviewId, LocalDateTime datePosted, Integer userId, Long postId, String review,
			Integer ranking) {
		super();
		this.reviewId = reviewId;
		this.datePosted = datePosted;
		this.userId = userId;
		this.postId = postId;
		this.setReview(review);
		this.ranking = ranking;
	}

	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}

	public void setDatePosted(LocalDateTime datePosted) {
		this.datePosted = datePosted;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	public String getReviewId() {
		return reviewId;
	}

	public LocalDateTime getDatePosted() {
		return datePosted;
	}

	public Integer getUserId() {
		return userId;
	}

	public Long getPostId() {
		return postId;
	}

	public Integer getRanking() {
		return ranking;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

}
