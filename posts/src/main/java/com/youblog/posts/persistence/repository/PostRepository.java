package com.youblog.posts.persistence.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.youblog.posts.persistence.model.Post;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
	Optional<Post> findById(Long id);
	Page<Post> findAllByOrderByDatePostedDesc(Pageable pageable);
}