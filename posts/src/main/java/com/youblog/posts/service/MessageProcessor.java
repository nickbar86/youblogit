package com.youblog.posts.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import com.youblog.posts.service.dto.PostDTO;
import com.youblog.util.Event;
import com.youblog.util.exceptions.EventProcessingException;

@EnableBinding(Sink.class)
public class MessageProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(MessageProcessor.class);

	private final PostService postService;

	@Autowired
	public MessageProcessor(PostService postService) {
		this.postService = postService;
	}

	@StreamListener(target = Sink.INPUT)
	public void process(Event<String, PostDTO> event) {

		LOG.info("Process message created at {}...", event.getEventCreatedAt());

		switch (event.getEventType()) {

		case CREATE:
			PostDTO post = event.getData();
			LOG.info("Creating a new post");
			postService.savePost(post);
			break;
			
		case DELETE:
			LOG.info("Deleting post with id:{}", event.getKey());
			postService.deletePost(Long.parseLong(event.getKey()));
			break;
			
		case UPDATE:
			LOG.info("Updating post with id:{}", event.getKey());
			postService.updatePost(event.getData());
			break;

		default:
			String errorMessage = "Incorrect event type: " + event.getEventType()
					+ ", expected a CREATE or DELETE event";
			LOG.warn(errorMessage);
			throw new EventProcessingException(errorMessage);
		}

		LOG.info("Message processing done!");
	}
}
