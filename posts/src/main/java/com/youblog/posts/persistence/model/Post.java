package com.youblog.posts.persistence.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "post")
public class Post {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
    @Version
    private int version;

	@Column(name = "port")
	private Integer port;

	@Column(name = "title")
	private String title;

	@Column(name = "summary",columnDefinition = "TEXT")
	private String summary;

	@Column(name = "content",columnDefinition = "TEXT")
	private String content;

	@Column(name = "date_posted")
	private LocalDateTime datePosted;
	
	@Column(name = "editor_name")
	private String editorName;
	
	
	public Post() {
		super();
	}

	public Post(Long id, Integer port, String title, String summary, String content, LocalDateTime datePosted, String editorName) {
		super();
		this.id = id;
		this.port = port;
		this.title = title;
		this.summary = summary;
		this.content = content;
		this.datePosted = datePosted;
		this.editorName = editorName;
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

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
