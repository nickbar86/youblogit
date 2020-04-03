package com.youblog.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = { "eureka.client.enabled=false", "spring.cloud.config.enabled=false" })
class UserServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
