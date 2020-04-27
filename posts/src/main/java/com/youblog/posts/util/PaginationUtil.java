package com.youblog.posts.util;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Utility class for handling pagination.
 *
 * <p>
 * Pagination uses the same principles as the
 * <a href="https://developer.github.com/v3/#pagination">GitHub API</a>, and
 * follow <a href="http://tools.ietf.org/html/rfc5988">RFC 5988 (Link
 * header)</a>.
 */
public final class PaginationUtil {

	private PaginationUtil() {
	}

	public static <T> HttpHeaders generatePaginationHttpHeaders(Page<T> page, String baseUrl,
			Optional<Map<String, Object>> queryParams) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
		String link = "";
		if ((page.getNumber() + 1) < page.getTotalPages()) {
			link = "<" + generateUri(baseUrl, page.getNumber() + 1, page.getSize(), queryParams) + ">; rel=\"next\",";
		}
		// prev link
		if ((page.getNumber()) > 0) {
			link += "<" + generateUri(baseUrl, page.getNumber() - 1, page.getSize(), queryParams) + ">; rel=\"prev\",";
		}
		// last and first link
		int lastPage = 0;
		if (page.getTotalPages() > 0) {
			lastPage = page.getTotalPages() - 1;
		}
		link += "<" + generateUri(baseUrl, lastPage, page.getSize(), queryParams) + ">; rel=\"last\",";
		link += "<" + generateUri(baseUrl, 0, page.getSize(), queryParams) + ">; rel=\"first\"";
		headers.add(HttpHeaders.LINK, link);
		return headers;
	}

	private static String generateUri(String baseUrl, int page, int size, Optional<Map<String, Object>> queryParams) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
		queryParams.ifPresent(qparam -> {
			qparam.entrySet().forEach(entry -> {
				builder.queryParam(entry.getKey(), entry.getValue());
			});
		});

		return builder.queryParam("page", page).queryParam("size", size).toUriString();
	}
}
