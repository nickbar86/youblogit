package com.youblog.posts.service.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.youblog.posts.persistence.model.Post;
import com.youblog.posts.service.dto.PostDTO;


@Mapper(componentModel = "spring")
public interface PostMapper {

	PostDTO entityToApi(Post entity);

	Post apiToEntity(PostDTO api);

	List<PostDTO> entityListToApiList(List<Post> entity);

	List<Post> apiListToEntityList(List<PostDTO> api);
}