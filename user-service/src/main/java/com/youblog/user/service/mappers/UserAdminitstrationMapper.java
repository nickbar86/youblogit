package com.youblog.user.service.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.youblog.user.persistence.model.User;
import com.youblog.user.service.dto.UserAdministration;

@Mapper(componentModel = "spring")
public interface UserAdminitstrationMapper {

	UserAdministration entityToApi(User entity);

	@Mappings({ @Mapping(target = "password", ignore = true),
			@Mapping(target = "version", ignore = true) })
	User apiToEntity(UserAdministration api);

	List<UserAdministration> entityListToApiList(List<User> entity);

	List<User> apiListToEntityList(List<UserAdministration> api);
}