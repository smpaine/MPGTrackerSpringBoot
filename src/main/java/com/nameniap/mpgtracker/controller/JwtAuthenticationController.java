package com.nameniap.mpgtracker.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nameniap.mpgtracker.config.JwtTokenUtil;
import com.nameniap.mpgtracker.model.JwtRequest;
import com.nameniap.mpgtracker.model.JwtResponse;
import com.nameniap.mpgtracker.model.User;
import com.nameniap.mpgtracker.repository.UserRepository;

@RestController
public class JwtAuthenticationController {
	
	Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserRepository userRepository;

	@PostMapping(value = "/api/authenticate")
	public ResponseEntity<?> generateAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
			throws Exception {

		authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());

		User user = userRepository.findByUserName(authenticationRequest.getUserName());
		
		if (user.getLastLoginDt() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			logger.info("Successful login for " + user.getUserName() + ". Last login date: " + sdf.format(user.getLastLoginDt()));
		} else {
			logger.info("Successful login for " + user.getUserName() + ". Last login date: <null>");
		}
		user.setLastLoginDt(Calendar.getInstance().getTime());
		userRepository.save(user);

		final String token = jwtTokenUtil.generateToken(user);

		return ResponseEntity.ok(new JwtResponse(token, user));
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}