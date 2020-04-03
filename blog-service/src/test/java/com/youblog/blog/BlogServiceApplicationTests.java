package com.youblog.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = { "eureka.client.enabled=false", "spring.cloud.config.enabled=false" })
//@TestPropertySource("/../../../../../config-repo/blog-service-test.yml")
//@ContextConfiguration(classes = BlogServiceApplication.class)
@AutoConfigureWebFlux
class BlogServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
