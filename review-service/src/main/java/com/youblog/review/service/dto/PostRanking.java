package com.youblog.review.service.dto;

public class PostRanking {

	private Integer postId;
	private Float avgRanking;

	public Integer getPostId() {
		return postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public Float getAvgRanking() {
		return avgRanking;
	}

	public void setAvgRanking(Float ranking) {
		this.avgRanking = ranking;
	}

}
