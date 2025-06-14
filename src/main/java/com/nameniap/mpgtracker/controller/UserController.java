package com.nameniap.mpgtracker.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nameniap.mpgtracker.config.WebSecurityConfig;
import com.nameniap.mpgtracker.exception.ResourceException;
import com.nameniap.mpgtracker.model.User;
import com.nameniap.mpgtracker.repository.UserRepository;

@RestController
public class UserController {
	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository users;

	@Autowired
	private WebSecurityConfig wsc;

	@GetMapping("/api/users")
	List<User> getAllUsers() {
		return this.users.findAll(Sort.by(new Order(Direction.ASC, "userName")));
	}

	@PostMapping("/api/users")
	User updatePassword(@RequestBody User userToUpdate) {
		if (userToUpdate != null) {
			logger.info("User service - recieved user: <" + userToUpdate.getId() + ", " + userToUpdate.getUserName()
					+ ", " + userToUpdate.getPassword() + ">");
			Optional<User> userFromDb = this.users.findById(userToUpdate.getId());

			if (userFromDb.isPresent()) {

				String encryptedPassword = this.wsc.passwordEncoder().encode(userToUpdate.getPassword());
				logger.info("Encrypted password: " + encryptedPassword);
				userFromDb.get().setPassword(encryptedPassword);
				this.users.save(userFromDb.get());
			}

			return userFromDb.get();
		} else {
			throw new ResourceException(HttpStatus.NOT_MODIFIED, "Error updating user");
		}
	}

	@PutMapping("/api/users")
	User addUser(@RequestBody User userToAdd) {
		if (userToAdd != null) {
			logger.info(
					"User service - recieved user: <" + userToAdd.getUserName() + ", " + userToAdd.getPassword() + ">");

			String encryptedPassword = this.wsc.passwordEncoder().encode(userToAdd.getPassword());
			logger.info("Encrypted password: " + encryptedPassword);
			userToAdd.setPassword(encryptedPassword);
			this.users.save(userToAdd);

			return userToAdd;
		} else {
			throw new ResourceException(HttpStatus.NOT_MODIFIED, "Error adding user");
		}
	}
	
	@DeleteMapping("/api/users/{id}")
	User deleteUser(@PathVariable int id) {
		if (id >= 0) {
			logger.info(
					"User service - received id to delete: " + id);
			
			Optional<User> userToDelete = this.users.findById(id);
			
			if (userToDelete.isPresent()) {
				this.users.delete(userToDelete.get());
				return userToDelete.get();
			} else {
				throw new ResourceException(HttpStatus.NOT_MODIFIED, "Error deleting user");
			}
		} else {
			throw new ResourceException(HttpStatus.NOT_MODIFIED, "Error deleting user");
		}
	}
	
	@GetMapping("/api/users/{id}")
	User lookupUserById(@PathVariable int id) {
		if (id >= 0) {
			logger.info(
					"User service - received id to lookup: " + id);
			
			Optional<User> aUser = this.users.findById(id);
			
			if (aUser.isPresent()) {
				return aUser.get();
			} else {
				throw new ResourceException(HttpStatus.NOT_FOUND, "User not found");
			}
		} else {
			throw new ResourceException(HttpStatus.NOT_FOUND, "User not found");
		}
	}

}
