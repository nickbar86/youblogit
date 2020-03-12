package com.youblog.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.youblog.user.service.dto.UserInfo;

@RequestMapping("users")
public interface IUser {
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping(path = "{id}", produces = "application/json")
	public ResponseEntity<UserInfo> getUserById(@PathVariable("id") Long id) ;
}
