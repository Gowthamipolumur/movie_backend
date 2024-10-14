package com.jts.movie.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.jts.movie.repositories.UserRepository;
import com.jts.movie.entities.User;
import java.util.ArrayList;

@Service
public class UserInfoUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public UserInfoUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmailId(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
		return new org.springframework.security.core.userdetails.User(user.getEmailId(), user.getPassword(),
				new ArrayList<>());  // Add any roles or authorities if applicable
	}
}
