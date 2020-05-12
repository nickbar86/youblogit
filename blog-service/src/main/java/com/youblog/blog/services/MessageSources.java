package com.youblog.blog.services;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessageSources {
	String OUTPUT_POSTS = "output-posts";
	String OUTPUT_REVIEWS = "output-reviews";

	@Output(OUTPUT_POSTS)
	MessageChannel outputPosts();

	@Output(OUTPUT_REVIEWS)
	MessageChannel outputReviews();
}
