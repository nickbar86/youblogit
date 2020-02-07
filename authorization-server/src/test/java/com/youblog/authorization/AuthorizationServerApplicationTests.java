package com.youblog.authorization;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(properties = { "eureka.client.enabled=false", "spring.cloud.config.enabled=false" })
@AutoConfigureMockMvc
class AuthorizationServerApplicationTests {

	@Autowired
	MockMvc mvc;

	@Test
	public void requestTokenWhenUsingPasswordGrantTypeThenOk() throws Exception {

		this.mvc.perform(post("/oauth/token").param("grant_type", "password").param("username", "magnus")
				.param("password", "password").header("Authorization", "Basic cmVhZGVyOnNlY3JldA=="))
				.andExpect(status().isOk());
	}

	@Test
	public void requestJwkSetWhenUsingDefaultsThenOk() throws Exception {

		this.mvc.perform(get("/.well-known/jwks.json")).andExpect(status().isOk());
	}
}
