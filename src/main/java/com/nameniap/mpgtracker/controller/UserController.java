package com.nameniap.mpgtracker.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	List<User> getAllVehicles() {
		return this.users.getAllUsers();
	}

	@PostMapping("/api/users")
	User updatePassword(@RequestBody User userToUpdate) {
		if (userToUpdate != null) {
			logger.info("User service - recieved user: <" + userToUpdate.getId() + ", " + userToUpdate.getUserName()
					+ ", " + userToUpdate.getPassword() + ">");
			User userFromDb = this.users.findById(userToUpdate.getId());

			if (userFromDb != null) {

				String encryptedPassword = this.wsc.passwordEncoder().encode(userToUpdate.getPassword());
				logger.info("Encrypted password: " + encryptedPassword);
				userFromDb.setPassword(encryptedPassword);
				this.users.save(userFromDb);
			}

			return userFromDb;
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
			
			User userToDelete = this.users.findById(id);
			
			if (userToDelete != null) {
				this.users.delete(userToDelete);
				return userToDelete;
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
			
			User aUser = this.users.findById(id);
			
			if (aUser != null) {
				return aUser;
			} else {
				throw new ResourceException(HttpStatus.NOT_FOUND, "User not found");
			}
		} else {
			throw new ResourceException(HttpStatus.NOT_FOUND, "User not found");
		}
	}

}
