package com.youblog.authorization;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.youblog.authorization.dto.BlogUser;
import com.youblog.authorization.proxies.UserServiceProxy;

public class DirectoryUserDetailsService implements UserDetailsService {

	private UserServiceProxy userService;

	public DirectoryUserDetailsService(UserServiceProxy userService) {
		super();
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			BlogUser blogUser = userService.findUserbyUsername(username);
			PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

			if (blogUser != null) {
				return CustomUser.buildCustomUserFromUserDets(User.withUsername(blogUser.getEmail())
						.password("{bcrypt}"+blogUser.getPassword())
						.accountLocked(!blogUser.isEnabled())
						.roles(blogUser.getRole()).build(), blogUser.getId());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		throw new UsernameNotFoundException(username);
	}

}
