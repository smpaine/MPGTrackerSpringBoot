package com.nameniap.mpgtracker.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.nameniap.mpgtracker.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	Logger logger = LoggerFactory.getLogger(UserDetailsService.class);
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {

		com.nameniap.mpgtracker.model.User userFromDb = userRepository.findUser(username);
		if (userFromDb != null) {
			return new User(userFromDb.getUserName(), userFromDb.getPassword(), new ArrayList<>());
		} else {
			logger.error("User not found with username: " + username);
			
			return null;
		}
	}

}