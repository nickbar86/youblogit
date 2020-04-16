package com.youblog.review.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import com.youblog.review.service.dto.ReviewDTO;
import com.youblog.util.Event;
import com.youblog.util.exceptions.EventProcessingException;

@EnableBinding(Sink.class)
public class MessageProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(MessageProcessor.class);

	private final ReviewService reviewService;

	@Autowired
	public MessageProcessor(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@StreamListener(target = Sink.INPUT)
	public void process(Event<String, ReviewDTO> event) {

		LOG.info("Process message created at {}...", event.getEventCreatedAt());

		switch (event.getEventType()) {

		case CREATE:
			ReviewDTO review = event.getData();
			review.setDatePosted(LocalDateTime.now());
			LOG.info("Creating a new review");
			reviewService.saveReview(review);
			break;

		case DELETE:
			if (event.getKey().startsWith("postId")) {
				LOG.info("Deleting post reviews with post id:{}", event.getKey());
				reviewService.deleteReviewsByPostId(Long.parseLong(event.getKey().split(":")[1]));
			} else {
				LOG.info("Deleting review with id:{}", event.getKey());
				reviewService.deleteReview(Long.parseLong(event.getKey()));
			}
			break;

		case UPDATE:
			LOG.info("Updating review with id:{}", event.getKey());
			reviewService.updateReview(event.getData());
			break;

		default:
			String errorMessage = "Incorrect event type: " + event.getEventType()
					+ ", expected a CREATE or DELETE or UPDATE event";
			LOG.warn(errorMessage);
			throw new EventProcessingException(errorMessage);
		}

		LOG.info("Message processing done!");
	}
}
