package com.youblog.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youblog.user.persistence.model.User;
import com.youblog.user.persistence.repository.UserRepository;
import com.youblog.user.service.dto.UserInfo;
import com.youblog.user.service.mappers.UserAdminitstrationMapper;
import com.youblog.user.service.mappers.UserInfoMapper;
import com.youblog.util.exceptions.NotFoundException;

@Service
@Transactional
public class UserService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private UserRepository repository;
	private UserAdminitstrationMapper mapper;
	private UserInfoMapper infoMapper;
	@Autowired
	public UserService(UserRepository repository, UserAdminitstrationMapper mapper) {
		super();
		this.repository = repository;
		this.mapper = mapper;
	}


	public UserInfo getUserById(Long id) {
		logger.info("Fetching User " + id);
		User entity = repository.findById(id)
				.orElseThrow(() -> new NotFoundException("No User found for userId: " + id));
		UserInfo response = infoMapper.entityToApi(entity);
		return response;
	}

}
