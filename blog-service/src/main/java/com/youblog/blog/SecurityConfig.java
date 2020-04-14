
package com.youblog.blog;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.http.HttpMethod.*;

@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.csrf().disable()
			.authorizeExchange()
				.pathMatchers("/actuator/**").permitAll()
				.pathMatchers(GET, "/blog-post").permitAll()
				.pathMatchers(GET, "/blog-post/posts/**").permitAll()
				.pathMatchers(GET, "/blog-post/reviews/**").permitAll()
				.pathMatchers(PUT, "/blog-post/users").permitAll()
				//.pathMatchers(POST, "/blog-post/**").hasAuthority("SCOPE_blogpost:write")
				//.pathMatchers(DELETE, "/blog-post/**").hasAuthority("SCOPE_blogpost:write")
				//.pathMatchers(PUT, "/blog-post/**").hasAuthority("SCOPE_blogpost:write")
				.anyExchange().authenticated()
				.and()
			.oauth2ResourceServer()
				.jwt();
		return http.build();
	}
}