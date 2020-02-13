package com.microservices.blog.service.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.microservices.blog.persistence.model.Post;
import com.microservices.blog.service.dto.PostDTO;

@Mapper(componentModel = "spring")
public interface PostMapper {

	@Mappings({ @Mapping(target = "ranking", ignore = true) })
	PostDTO entityToApi(Post entity);

	Post apiToEntity(PostDTO api);

	List<PostDTO> entityListToApiList(List<Post> entity);

	List<Post> apiListToEntityList(List<PostDTO> api);
}