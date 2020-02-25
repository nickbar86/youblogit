package com.youblog.blog.services.dto;

import java.time.LocalDateTime;

public class PostRankingDTO extends PostDTO {
	private Float ranking;

	public PostRankingDTO(long id, String port, String title, String summary, String content, LocalDateTime datePosted,
			String editorName, Float ranking) {
		super(id, port, title, summary, content, datePosted, editorName);
		this.setRanking(ranking);
	}

	public Float getRanking() {
		return ranking;
	}

	public void setRanking(Float ranking) {
		this.ranking = ranking;
	}

}
