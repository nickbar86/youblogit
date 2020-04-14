package com.youblog.user.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.youblog.user.persistence.model.BlogUser;


@Repository
public interface BlogUserRepository extends CrudRepository<BlogUser, Integer>,BlogUserRepositoryCustom {
	public BlogUser findByEmailIgnoreCase(@Param("email") String email);
	
	@Override
	@RestResource(exported = false)
	<S extends BlogUser> Iterable<S> saveAll(Iterable<S> entities);
}
