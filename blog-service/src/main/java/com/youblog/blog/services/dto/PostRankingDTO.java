package com.youblog.blog.services.dto;

import java.time.LocalDateTime;

public class PostRankingDTO extends PostDTO {
	private Float ranking;
	private BlogUserDetails user;

	public PostRankingDTO(long id, String port, String title, String summary, String content, LocalDateTime datePosted, LocalDateTime dateUpdated,
			Integer userId, Float ranking) {
		super(id, port, title, summary, content, datePosted, datePosted, userId);
		this.setRanking(ranking);
	}

	public Float getRanking() {
		return ranking;
	}

	public void setRanking(Float ranking) {
		this.ranking = ranking;
	}

	public BlogUserDetails getUser() {
		return user;
	}

	public void setUser(BlogUserDetails user) {
		this.user = user;
	}
}
