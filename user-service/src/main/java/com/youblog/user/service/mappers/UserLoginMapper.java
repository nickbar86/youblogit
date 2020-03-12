package com.youblog.user.service.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.youblog.user.persistence.model.User;
import com.youblog.user.service.dto.UserLogin;
@Mapper(componentModel = "spring")
public interface UserLoginMapper {
	UserLogin entityToApi(User entity);

	@Mappings({ @Mapping(target = "password", ignore = true),
			@Mapping(target = "version", ignore = true), 
			@Mapping(target = "id", ignore = true),
			@Mapping(target = "dateCreated", ignore = true),
			@Mapping(target = "role", ignore = true),
			@Mapping(target = "nickName", ignore = true)
	})
	User apiToEntity(UserLogin api);

	List<UserLogin> entityListToApiList(List<User> entity);

	List<User> apiListToEntityList(List<UserLogin> api);
}
