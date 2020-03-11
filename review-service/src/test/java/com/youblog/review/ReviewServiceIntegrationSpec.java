package com.youblog.review;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("Review Service Integration Tests")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "eureka.client.enabled=false",
		"spring.cloud.config.enabled=false", "spring.datasource.url=jdbc:h2:mem:review-db" })
public class ReviewServiceIntegrationSpec {

}
