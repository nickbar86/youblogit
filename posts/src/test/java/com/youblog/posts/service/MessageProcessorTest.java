package com.youblog.posts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.youblog.posts.persistence.model.Post;
import com.youblog.posts.persistence.repository.PostRepository;
import com.youblog.posts.service.dto.PostDTO;
import com.youblog.posts.service.mappers.PostMapper;
import com.youblog.util.Event;
import com.youblog.util.Event.Type;
import com.youblog.util.exceptions.InvalidInputException;

@DisplayName("Test Message Processor")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "eureka.client.enabled=false",
		"spring.cloud.config.enabled=false", "spring.datasource.url=jdbc:h2:mem:review-db" })
@Rollback
public class MessageProcessorTest {
	private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorTest.class);


	@Autowired
	private PostRepository repository;

	@Autowired
	private Sink channels;

	@Autowired
	private PostMapper mappers;

	private AbstractMessageChannel input = null;

	@BeforeEach
	public void setupDb() {
		input = (AbstractMessageChannel) channels.input();
		repository.deleteAll();
	}
	
	@AfterEach
	public void clear() {
		repository.deleteAll();
	}
	
	@Test
	public void testCreateReviewSuccess() {
		assertEquals(0, repository.findAll(PageRequest.of(0, 10)).getContent().size());
		Long postId1 = 1L;
		Long postId2 = 2L;
		Long postId3 = 3L;
		assertTrue(!repository.findById(postId1).isPresent());
		assertTrue(!repository.findById(postId2).isPresent());
		assertTrue(!repository.findById(postId3).isPresent());
		sendCreatePostEvent();
		sendCreatePostEvent();
		sendCreatePostEvent();
		assertEquals(3, repository.findAll(PageRequest.of(0, 10)).getContent().size());
	}
	
	@Test
	public void deleteReviews() {
		assertEquals(0, repository.findAll(PageRequest.of(0, 10)).getContent().size());
		sendCreatePostEvent();
		Page<Post> page = repository.findAll(PageRequest.of(0, 10));
		assertEquals(1, page.getContent().size());
		sendDeletePostEvent(page.getContent().get(0).getId());
		assertTrue(!repository.findById(page.getContent().get(0).getId()).isPresent());
	}
	
	@Test
	public void testUpdateReviewSuccess() {
		assertEquals(0, repository.findAll(PageRequest.of(0, 10)).getContent().size());
		sendCreatePostEvent();
		Page<Post> page = repository.findAll(PageRequest.of(0, 10));
		assertEquals(1, page.getContent().size());
		sendUpdatePostEvent(page.getContent().get(0).getId());
		assertEquals("Test 2",repository.findById(page.getContent().get(0).getId()).get().getTitle());
	}
	

	private void sendCreatePostEvent() {
		Post entity = new Post();
		entity.setContent("This is a test 1");
		entity.setDatePosted(LocalDateTime.now());
		entity.setEditorName("Test Editor 1");
		entity.setPort(123);
		entity.setSummary("Test Summary 1");
		entity.setTitle("Test 1");
		PostDTO dto = mappers.entityToApi(entity);
		Event<Integer, PostDTO> event = new Event(Type.CREATE, dto.getTitle(), dto);
		input.send(new GenericMessage<>(event));
	}

	private void sendDeletePostEvent(Long postId) {
		Event<String, PostDTO> event = new Event(Type.DELETE, ""+postId, null);
		input.send(new GenericMessage<>(event));
	}

	private void sendUpdatePostEvent(Long postId) {
		Post entityupdated = new Post();
		entityupdated.setContent("This is a test 2");
		entityupdated.setDatePosted(LocalDateTime.now());
		entityupdated.setEditorName("Test Editor 2");
		entityupdated.setPort(123);
		entityupdated.setSummary("Test Summary 2");
		entityupdated.setTitle("Test 2");
		entityupdated.setId(postId);
		PostDTO dto = mappers.entityToApi(entityupdated);
		Event<Integer, PostDTO> event = new Event(Type.UPDATE, ""+dto.getId(), dto);
		input.send(new GenericMessage<>(event));
	}
}
