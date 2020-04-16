package com.youblog.authorization;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUser extends User {
	private Integer blogUserId;

	private CustomUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	public Integer getBlogUserId() {
		return blogUserId;
	}

	public void setBlogUserId(Integer blogUserId) {
		this.blogUserId = blogUserId;
	}
	
	public static User buildCustomUserFromUserDets(UserDetails userDetails, Integer blogUserId) {
		CustomUser customUser = new CustomUser(userDetails.getUsername(), userDetails.getPassword(), userDetails.isEnabled(), userDetails.isAccountNonExpired(), userDetails.isCredentialsNonExpired(), userDetails.isAccountNonLocked(), userDetails.getAuthorities());
		customUser.setBlogUserId(blogUserId);
		return customUser;
	}

}
