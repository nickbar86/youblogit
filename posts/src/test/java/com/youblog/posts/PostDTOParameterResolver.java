package com.youblog.posts;

import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import com.youblog.posts.service.dto.PostDTO;

public class PostDTOParameterResolver implements ParameterResolver {

	private final Map<String, PostDTO> posts ;

	public PostDTOParameterResolver() {
		Map<String, PostDTO> posts = new HashMap<>();
		PostDTO dto = new PostDTO();
		dto.setContent("This is a test");
		dto.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 1));
		dto.setEditorName("Test Editor");
		dto.setPort(123);
		dto.setSummary("Test Summary");
		dto.setTitle("Test 1");
		dto.setId(1L);
		posts.put("Test 1", dto);
		dto = new PostDTO();
		dto.setContent("This is a test 2");
		dto.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 2));
		dto.setEditorName("Test Editor 2");
		dto.setPort(123);
		dto.setSummary("Test Summary 2");
		dto.setTitle("Test 2");
		dto.setId(2L);
		posts.put("Test 2", dto);
		
		dto = new PostDTO();
		dto.setContent("This is a test 3");
		dto.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 3));
		dto.setEditorName("Test Editor 3");
		dto.setPort(123);
		dto.setSummary("Test Summary 3");
		dto.setTitle("Test 3");
		dto.setId(3L);
		posts.put("Test 3", dto);
		
		dto = new PostDTO();
		dto.setContent("This is a test 4");
		dto.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 4));
		dto.setEditorName("Test Editor 4");
		dto.setPort(123);
		dto.setSummary("Test Summary 4");
		dto.setTitle("Test 4");
		dto.setId(4L);
		posts.put("Test 4", dto);
		
		dto = new PostDTO();
		dto.setContent("This is a test 2");
		dto.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 5));
		dto.setEditorName("Test Editor 5");
		dto.setPort(123);
		dto.setSummary("Test Summary 5");
		dto.setTitle("Test 5");
		dto.setId(5L);
		posts.put("Test 5", dto);
		
		dto = new PostDTO();
		dto.setContent("This is a test 6");
		dto.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 6));
		dto.setEditorName("Test Editor 6");
		dto.setPort(123);
		dto.setSummary("Test Summary 6");
		dto.setTitle("Test 6");
		dto.setId(6L);
		posts.put("Test 6", dto);
		
		dto = new PostDTO();
		dto.setContent("This is a test 7");
		dto.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 7));
		dto.setEditorName("Test Editor 7");
		dto.setPort(123);
		dto.setSummary("Test Summary 7");
		dto.setTitle("Test 7");
		dto.setId(7L);
		posts.put("Test 7", dto);
		
		dto = new PostDTO();
		dto.setContent("This is a test 8");
		dto.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 8));
		dto.setEditorName("Test Editor 8");
		dto.setPort(123);
		dto.setSummary("Test Summary 8");
		dto.setTitle("Test 8");
		dto.setId(8L);
		posts.put("Test 8", dto);
		
		dto = new PostDTO();
		dto.setContent("This is a test 9");
		dto.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 9));
		dto.setEditorName("Test Editor 9");
		dto.setPort(123);
		dto.setSummary("Test Summary 9");
		dto.setTitle("Test 9");
		dto.setId(9L);
		posts.put("Test 9", dto);
		
		dto = new PostDTO();
		dto.setContent("This is a test 10");
		dto.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 10));
		dto.setEditorName("Test Editor 10");
		dto.setPort(123);
		dto.setSummary("Test Summary 10");
		dto.setTitle("Test 10");
		dto.setId(10L);
		posts.put("Test 10", dto);
		
		dto = new PostDTO();
		dto.setContent("This is a test 11");
		dto.setDatePosted(LocalDateTime.of(2020, 1, 1, 1, 11));
		dto.setEditorName("Test Editor 11");
		dto.setPort(123);
		dto.setSummary("Test Summary 11");
		dto.setTitle("Test 11");
		dto.setId(11L);
		posts.put("Test 11", dto);
		
		this.posts=posts;
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		Parameter parameter = parameterContext.getParameter();
		return Objects.equals(parameter.getParameterizedType().getTypeName(),
				"java.util.Map<java.lang.String, com.youblog.posts.service.dto.PostDTO>");
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return posts;
	}

}
