package com.youblog.user.persistence.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.youblog.user.persistence.model.BlogUser;

@Repository
@Transactional(readOnly = false)
public class BlogUserRepositoryImpl implements BlogUserRepositoryCustom{

	@PersistenceContext
    EntityManager entityManager;
	BCryptPasswordEncoder encode;
	
	public BlogUserRepositoryImpl( BCryptPasswordEncoder encode) {
		super();
		this.encode = encode;
	}

	@Override
	public BlogUser save(BlogUser user) {
		if(user.getId()!=null) {
			BlogUser persisted = entityManager.find(BlogUser.class, user.getId());
			persisted.setPassword(encode.encode(user.getPassword()));
			persisted.setEmail(user.getEmail());
			persisted.setEnabled(user.isEnabled());
			persisted.setName(user.getName());
			persisted.setRole(user.getRole());
			entityManager.persist(persisted);
			return persisted;
		}else {
			user.setPassword(encode.encode(user.getPassword()));
			entityManager.persist(user);
			return user;
		}
	}

}
