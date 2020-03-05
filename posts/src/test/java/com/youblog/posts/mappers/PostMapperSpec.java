package com.youblog.posts.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import com.youblog.posts.PostDTOParameterResolver;
import com.youblog.posts.PostParameterResolver;
import com.youblog.posts.persistence.model.Post;
import com.youblog.posts.service.dto.PostDTO;
import com.youblog.posts.service.mappers.PostMapper;

@DisplayName("Testing Transformation of Entity Post to DTO")
@ExtendWith(PostParameterResolver.class)
@ExtendWith(PostDTOParameterResolver.class)
public class PostMapperSpec {
	private PostMapper mapper = Mappers.getMapper(PostMapper.class);
	private PostDTO dto;
	private Post entity;

	@BeforeEach
	void init(Map<String, Post> posts, Map<String, PostDTO> dtos) {
		entity = posts.get("Test 1");
		dto = dtos.get("Test 1");
	}

	@Test
	@DisplayName("With a given PostDTO check that Post Entity is produced correctly")
	public void givenPostDTOCorrectPost() {
		Post newEntity = mapper.apiToEntity(dto);
		assertEquals(dto.getContent(), newEntity.getContent());
		assertEquals(dto.getDatePosted(), newEntity.getDatePosted());
		assertEquals(dto.getEditorName(), newEntity.getEditorName());
		assertEquals(dto.getPort(), newEntity.getPort());
		assertEquals(dto.getSummary(), newEntity.getSummary());
		assertEquals(dto.getTitle(), newEntity.getTitle());
		assertEquals(dto.getTitle(), newEntity.getTitle());
	}

	@Test
	@DisplayName("With a given PostEntity check that PostDTO is produced correctly")
	public void givenPostEntityCorrectPostDTO() {
		PostDTO newDto = mapper.entityToApi(entity);
		assertEquals(newDto.getContent(), entity.getContent());
		assertEquals(newDto.getDatePosted(), entity.getDatePosted());
		assertEquals(newDto.getEditorName(), entity.getEditorName());
		assertEquals(newDto.getPort(), entity.getPort());
		assertEquals(newDto.getSummary(), entity.getSummary());
		assertEquals(newDto.getTitle(), entity.getTitle());
	}
}
