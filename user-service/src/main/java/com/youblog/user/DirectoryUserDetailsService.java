package com.youblog.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.youblog.user.persistence.model.BlogUser;
import com.youblog.user.persistence.repository.BlogUserRepository;

public class DirectoryUserDetailsService implements UserDetailsService {

	private BlogUserRepository userRepossitory;

	public DirectoryUserDetailsService(BlogUserRepository userRepossitory) {
		super();
		this.userRepossitory = userRepossitory;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			final BlogUser blogUser = userRepossitory.findByEmailIgnoreCase(username);
			System.out.println("getting blog user");
			if (blogUser != null) {
				System.out.println(blogUser.getEmail());
				return User.withUsername(blogUser.getEmail()).accountLocked(!blogUser.isEnabled()).password(blogUser.getPassword())
						.roles(blogUser.getRole()).build();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		throw new UsernameNotFoundException(username);
	}

}
