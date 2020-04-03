package com.youblog.user.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.youblog.user.persistence.model.BlogUser;

public interface BlogUserRepository extends CrudRepository<BlogUser, Integer> {
	public BlogUser findByEmailIgnoreCase(@Param("email") String email);
}
