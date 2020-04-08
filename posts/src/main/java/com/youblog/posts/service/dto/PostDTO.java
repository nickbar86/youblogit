package com.youblog.posts.service.dto;

import java.time.LocalDateTime;

public class PostDTO {
	private Long id;
	private Integer port;
	private String title;
	private String summary;
	private String content;
	private LocalDateTime datePosted;
	private LocalDateTime dateUpdated;
	private Integer blogUserId;

	public PostDTO(long id, int port, String title, String summary, String content, LocalDateTime datePosted, LocalDateTime dateUpdated, Integer blogUserId) {
		this.id = id;
		this.port = port;
		this.title = title;
		this.summary = summary;
		this.content = content;
		this.datePosted = datePosted;
		this.dateUpdated = dateUpdated;
		this.blogUserId = blogUserId;
	}
	
	

	public PostDTO() {
		super();
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getDatePosted() {
		return datePosted;
	}

	public void setDatePosted(LocalDateTime datePosted) {
		this.datePosted = datePosted;
	}



	public LocalDateTime getDateUpdated() {
		return dateUpdated;
	}



	public void setDateUpdated(LocalDateTime dateUpdated) {
		this.dateUpdated = dateUpdated;
	}



	public Integer getBlogUserId() {
		return blogUserId;
	}



	public void setBlogUserId(Integer blogUserId) {
		this.blogUserId = blogUserId;
	}


}
