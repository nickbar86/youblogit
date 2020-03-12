package com.youblog.user.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.youblog.user.persistence.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	Page<User> findByNickNameOrEmail(Pageable pageable);
}
