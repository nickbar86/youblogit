package com.youblog.user.persistence.repository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.youblog.user.persistence.model.BlogUser;

@RepositoryRestResource(collectionResourceRel = "blogUsers", path = "blogUsers")
public interface BlogUserRepositoryCustom {
	
	@RestResource(path = "secureSave",exported = true, rel = "customSaveMethod")
	BlogUser save(BlogUser user);
}
