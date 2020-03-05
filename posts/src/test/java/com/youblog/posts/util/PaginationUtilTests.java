package com.youblog.posts.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class PaginationUtilTests {
	static final Logger LOGGER = LoggerFactory.getLogger(PaginationUtilTests.class);
	@Test
	public void generatePaginationHttpHeadersTest() {
		LOGGER.info("start");
		String baseUrl = "/api/_search/example";
		List<String> content = new ArrayList<>();
		Page<String> page = new PageImpl<>(content, PageRequest.of(6, 50), 400L);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, baseUrl);
		List<String> strHeaders = headers.get(HttpHeaders.LINK);
		assertNotNull(strHeaders);
		assertTrue(strHeaders.size() == 1);
		String headerData = strHeaders.get(0);
		assertTrue(headerData.split(",").length == 4);
		String expectedData = "</api/_search/example?page=7&size=50>; rel=\"next\","
				+ "</api/_search/example?page=5&size=50>; rel=\"prev\","
				+ "</api/_search/example?page=7&size=50>; rel=\"last\","
				+ "</api/_search/example?page=0&size=50>; rel=\"first\"";
		assertEquals(expectedData, headerData);
		List<String> xTotalCountHeaders = headers.get("X-Total-Count");
		assertTrue(xTotalCountHeaders.size() == 1);
		assertTrue(Long.valueOf(xTotalCountHeaders.get(0)).equals(400L));
	}
}
