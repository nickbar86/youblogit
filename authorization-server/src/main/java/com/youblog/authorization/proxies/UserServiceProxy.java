package com.youblog.authorization.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.youblog.authorization.FeignClientConfiguration;
import com.youblog.authorization.dto.BlogUser;


@FeignClient(name = "user-service",configuration = FeignClientConfiguration.class)
public interface UserServiceProxy {
	@RequestMapping(method = RequestMethod.GET, value = "/blogUsers/search/findByEmailIgnoreCase")
	BlogUser findUserbyUsername(@RequestParam("email") String email);
}
