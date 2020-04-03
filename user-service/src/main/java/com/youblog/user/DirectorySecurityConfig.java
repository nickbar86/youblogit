package com.youblog.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.youblog.user.persistence.repository.BlogUserRepository;

@Configuration
public class DirectorySecurityConfig extends WebSecurityConfigurerAdapter {
	BlogUserRepository userRepository;

	@Autowired
	public DirectorySecurityConfig(BlogUserRepository up) {
		userRepository = up;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/**").hasRole("ADMIN").and().httpBasic();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(new DirectoryUserDetailsService(this.userRepository));
	}

}
