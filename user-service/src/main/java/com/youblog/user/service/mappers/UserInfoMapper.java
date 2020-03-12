package com.youblog.user.service.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.youblog.user.persistence.model.User;
import com.youblog.user.service.dto.UserInfo;


@Mapper(componentModel = "spring")
public interface UserInfoMapper {

	UserInfo entityToApi(User entity);

	@Mappings({ @Mapping(target = "password", ignore = true),
			@Mapping(target = "version", ignore = true), 
			@Mapping(target = "id", ignore = true),
			@Mapping(target = "dateCreated", ignore = true)
	})
	User apiToEntity(UserInfo api);

	List<UserInfo> entityListToApiList(List<User> entity);

	List<User> apiListToEntityList(List<UserInfo> api);
}
