package com.youblog.blog.services.dto;

public class PostRanking {

	private Long postId;
	private Float avgRanking=0F;

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Float getAvgRanking() {
		return avgRanking;
	}

	public void setAvgRanking(Float ranking) {
		this.avgRanking = ranking;
	}

}
