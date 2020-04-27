package com.youblog.blog.services.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDTO {
	private Long id;
	private String port;
	private String title;
	private String summary;
	private String content;
	private LocalDateTime datePosted;
	private LocalDateTime dateUpdated;
	private Integer blogUserId;

	protected PostDTO(PostDTOBuilder<?> builder) {
		this.id = builder.id;
		this.port = builder.port;
		this.title = builder.title;
		this.summary = builder.summary;
		this.content = builder.content;
		this.datePosted = builder.datePosted;
		this.blogUserId = builder.blogUserId;
		this.setDateUpdated(builder.dateUpdated);
	}

	public static class PostDTOBuilder<T extends PostDTOBuilder<T>> {
		private Long id;
		private String port;
		private String title;
		private String summary;
		private String content;
		private LocalDateTime datePosted;
		private LocalDateTime dateUpdated;
		private Integer blogUserId;

		public static PostDTOBuilder withDtoBuilder() {
			return new PostDTOBuilder();
		}

		protected PostDTOBuilder() {

		}
		
		@SuppressWarnings("unchecked")
		public  T id(long id) {
			this.id=id;
			return (T)this;
		}
		
		@SuppressWarnings("unchecked")
		public  T port(String port) {
			this.port=port;
			return (T)this;
		}
		
		@SuppressWarnings("unchecked")
		public  T title(String title) {
			this.title=title;
			return (T)this;
		}
		
		@SuppressWarnings("unchecked")
		public  T summary(String summary) {
			this.summary=summary;
			return (T)this;
		}
		
		@SuppressWarnings("unchecked")
		public  T content(String content) {
			this.content=content;
			return (T)this;
		}
		
		@SuppressWarnings("unchecked")
		public  T datePosted(LocalDateTime datePosted) {
			this.datePosted=datePosted;
			return (T)this;
		}
		
		@SuppressWarnings("unchecked")
		public  T dateUpdated(LocalDateTime dateUpdated) {
			this.dateUpdated=dateUpdated;
			return (T)this;
		}
		
		@SuppressWarnings("unchecked")
		public T blogUserId(Integer blogUserId) {
			this.blogUserId=blogUserId;
			return (T)this;
		}
		
		public PostDTO build() 
        { 
            return new PostDTO(this); 
        } 
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

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
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

	public Integer getBlogUserId() {
		return blogUserId;
	}

	public void setBlogUserId(Integer blogUserId) {
		this.blogUserId = blogUserId;
	}

	public LocalDateTime getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(LocalDateTime dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

}
