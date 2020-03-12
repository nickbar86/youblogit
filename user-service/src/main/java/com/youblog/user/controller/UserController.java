package com.youblog.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.youblog.user.api.IUser;
import com.youblog.user.service.UserService;
import com.youblog.user.service.dto.UserInfo;
import com.youblog.util.exceptions.NotFoundException;

@RestController
public class UserController implements IUser {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService service;

	@Override
	public ResponseEntity<UserInfo> getUserById(Long id) {
		logger.info("Fetching Post by id" + id);
		try {
			return ResponseEntity.status(HttpStatus.OK).body(service.getUserById(id));
		} catch (NotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
	}
}
