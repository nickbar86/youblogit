package com.youblog.posts.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youblog.posts.PostDTOParameterResolver;
import com.youblog.posts.PostParameterResolver;
import com.youblog.posts.controller.PostController;
import com.youblog.posts.persistence.model.Post;
import com.youblog.posts.persistence.repository.PostRepository;
import com.youblog.posts.service.PostService;
import com.youblog.posts.service.dto.PostDTO;
import com.youblog.posts.service.mappers.PostMapper;
import com.youblog.util.exceptions.NotFoundException;

@WebMvcTest(controllers = PostController.class, properties = { "eureka.client.enabled=false",
		"spring.cloud.config.enabled=false" })
@ActiveProfiles("test")
@ExtendWith(PostParameterResolver.class)
@ExtendWith(PostDTOParameterResolver.class)
@DisplayName("Integration tests For Post Controller")
public class PostControllerITest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private PostMapper mappers;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	PostService service;
	@MockBean
	PostRepository repo;
	private List<Post> originalPosts;
	private List<PostDTO> originalPostsDto;

	@BeforeEach
	void init(Map<String, Post> originalPosts, Map<String, PostDTO> originalPostsDto) {
		this.originalPosts = originalPosts.entrySet().stream().map(Entry::getValue).collect(Collectors.toList());
		this.originalPostsDto = originalPostsDto.entrySet().stream().map(Entry::getValue).collect(Collectors.toList());
	}

	@Test
	@DisplayName("GET /posts/ Check retrieve posts with paging params return 200 ")
	void whenValidPage_thenReturns200() throws Exception {
		Pageable pageRequest = PageRequest.of(0, 10);
		List<PostDTO> dtoListPaged = originalPostsDto.stream().limit(10).collect(Collectors.toList());
		List<Post> listPaged = originalPosts.stream().limit(10).collect(Collectors.toList());
		Page<PostDTO> pageDto = new PageImpl<>(dtoListPaged, pageRequest, 10);
		Page<Post> page = new PageImpl<>(listPaged, pageRequest, 10);
		Mockito.when(service.retrievePosts(pageRequest)).thenReturn(pageDto);
		Mockito.when(repo.findAllByOrderByDatePostedDesc(pageRequest)).thenReturn(page);
		MvcResult result = mockMvc.perform(get("/posts/?page=0&size=10"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;"))
				.andReturn();
		assertTrue(result.getResponse().getContentAsString().equals(objectMapper.writeValueAsString(dtoListPaged)));

	}

	@Test
	@DisplayName("GET /posts/ Check retrieve posts with no paging params return 200 and default 20 results")
	void whenNullPageable_thenReturns200() throws Exception {
		Pageable pageRequest = PageRequest.of(0, 20);
		List<PostDTO> dtoListPaged = originalPostsDto.stream().limit(20).collect(Collectors.toList());
		List<Post> listPaged = originalPosts.stream().limit(20).collect(Collectors.toList());
		Page<PostDTO> pageDto = new PageImpl<>(dtoListPaged, pageRequest, dtoListPaged.size());
		Page<Post> page = new PageImpl<>(listPaged, pageRequest, listPaged.size());
		Mockito.when(service.retrievePosts(pageRequest)).thenReturn(pageDto);
		Mockito.when(repo.findAllByOrderByDatePostedDesc(pageRequest)).thenReturn(page);
		MvcResult result = mockMvc.perform(get("/posts/"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;"))
				.andReturn();
		assertTrue(result.getResponse().getContentAsString().equals(objectMapper.writeValueAsString(dtoListPaged)));
	}
	
	@Test
	@DisplayName("GET /posts/{id} Check get post with valid id return PageDTO Object and 200")
	void givenIdForExistingObjectReturnObject() throws Exception {
		Post orginalPost = originalPosts.get(0);
		PostDTO orifinalPostDto = originalPostsDto.get(0);
		Mockito.when(service.getPost(orifinalPostDto.getId())).thenReturn(orifinalPostDto);
		Mockito.when(repo.findById(orginalPost.getId())).thenReturn(Optional.of(orginalPost));
		MvcResult result = mockMvc.perform(get("/posts/{id}",orginalPost.getId()))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json;"))
		.andReturn();
		assertTrue(result.getResponse().getContentAsString().equals(objectMapper.writeValueAsString(orifinalPostDto)));
	}
	
	@Test
	@DisplayName("GET /posts/{id} Check get post for non existent object and return 204")
	void givenIdForNonExistingReturnError() throws Exception {
		Mockito.when(service.getPost(100L)).thenThrow(new NotFoundException("No Post found for postId: " + 1));
		Mockito.when(repo.findById(100L)).thenReturn(Optional.ofNullable(null));
		mockMvc.perform(get("/posts/{id}",100L))
			.andExpect(status().isNoContent());
	}
	
	@Test
	@DisplayName("PUT /posts/ Send Valid Create PostDTO and expect PostDTO with id and 201")
	void givenPostDTOCreatePostAndReturnPostDTO() throws Exception {
		Post entity = new Post();
		entity.setContent("This is a test 1");
		entity.setEditorName("Test Editor 1");
		entity.setPort(123);
		entity.setSummary("Test Summary 1");
		entity.setTitle("Test 1");
		Post entityWithId = new Post();
		entityWithId.setContent("This is a test 1");
		entityWithId.setDatePosted(LocalDateTime.now());
		entityWithId.setEditorName("Test Editor 1");
		entityWithId.setPort(123);
		entityWithId.setSummary("Test Summary 1");
		entityWithId.setTitle("Test 1");
		entityWithId.setId(1L);
		entityWithId.setVersion(0);
		PostDTO dto = mappers.entityToApi(entity);
		PostDTO dtoWithId = mappers.entityToApi(entityWithId);
		Mockito.when(service.savePost(org.mockito.ArgumentMatchers.any(PostDTO.class))).thenReturn(dtoWithId);
		Mockito.when(repo.save(entity)).thenReturn(entityWithId);
		MvcResult result = mockMvc.perform(put("/posts/").content(objectMapper.writeValueAsString(dto)).contentType("application/json"))
		.andExpect(status().isCreated())
		.andExpect(content().contentType("application/json"))
		.andReturn();
		assertTrue(result.getResponse().getContentAsString().equals(objectMapper.writeValueAsString(dtoWithId)));
	}
	
	
	@Test
	@DisplayName("PUT /posts/ Send empty PostDTO and expect 400")
	void givenEmptyDTOReturnError() throws Exception {
		Mockito.when(service.savePost(null)).thenThrow(new NullPointerException());
		mockMvc.perform(put("/posts/").contentType("application/json"))
		.andExpect(status().isBadRequest())
		.andReturn();
	}
	
	@Test
	@DisplayName("POST /posts/ Send Valid Update PostDTO and expect PostDTO with id and 200")
	void givenPostDTOUpdatePostAndReturnPostDTOUpdated() throws Exception {
		Post entityupdated = new Post();
		entityupdated.setContent("This is a test 1");
		entityupdated.setDatePosted(LocalDateTime.now());
		entityupdated.setEditorName("Test Editor 1");
		entityupdated.setPort(123);
		entityupdated.setSummary("Test Summary 1");
		entityupdated.setTitle("Test 1");
		entityupdated.setId(1L);
		entityupdated.setVersion(0);
		PostDTO dtoUpdated = mappers.entityToApi(entityupdated);
		Mockito.when(service.updatePost(org.mockito.ArgumentMatchers.any(PostDTO.class))).thenReturn(dtoUpdated);
		Mockito.when(repo.save(org.mockito.ArgumentMatchers.any(Post.class))).thenReturn(entityupdated);
		MvcResult result = mockMvc.perform(post("/posts/").content(objectMapper.writeValueAsString(dtoUpdated)).contentType("application/json"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("application/json"))
		.andReturn();
		assertTrue(result.getResponse().getContentAsString().equals(objectMapper.writeValueAsString(dtoUpdated)));
	}
	
	
	@Test
	@DisplayName("POST /posts/ Send empty PostDTO and expect 400")
	void givenEmptyDTOToUpdateReturnError() throws Exception {
		Mockito.when(service.savePost(null)).thenThrow(new NotFoundException());
		mockMvc.perform(post("/posts/").contentType("application/json"))
		.andExpect(status().isBadRequest())
		.andReturn();
	}
}
