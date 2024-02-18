package org.jsp.merchantproductapp.service;

import java.util.Optional;

import org.jsp.merchantproductapp.dao.UserDao;
import org.jsp.merchantproductapp.dto.ResponseStructure;
import org.jsp.merchantproductapp.dto.User;
import org.jsp.merchantproductapp.exception.InvalidCredentialsException;
import org.jsp.merchantproductapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public ResponseEntity<ResponseStructure<User>> registerUser(User user) {
		ResponseStructure<User> structure = new ResponseStructure<>();
		try {
			// Check for duplicate data before attempting to save
			validateUniqueConstraints(user);

			// If validation passes, save the merchant
			structure.setData(userDao.registerUser(user));
			structure.setMessage("User Saved");
			structure.setStatusCode(HttpStatus.CREATED.value());

			return new ResponseEntity<>(structure, HttpStatus.CREATED);
		} catch (DuplicateKeyException e) {
			// Handle duplicate data exception
			structure.setMessage(e.getMessage());
			structure.setStatusCode(HttpStatus.CONFLICT.value());

			return new ResponseEntity<>(structure, HttpStatus.CONFLICT);
		}
	}

	private void validateUniqueConstraints(User user) {
		// Check if a merchant with the given email already exists
		if (userDao.existsByEmail(user.getEmail())) {
			throw new DuplicateKeyException("Duplicate email found.");
		}

		// Check if a merchant with the given phone number already exists
		if (userDao.existsByPhone(user.getPhone())) {
			throw new DuplicateKeyException("Duplicate phone number found.");
		}

	}

	public ResponseEntity<ResponseStructure<User>> updateUser(User user) {
		ResponseStructure<User> structure = new ResponseStructure<>();
		Optional<User> recUser = userDao.getUserById(user.getId());
		if (recUser.isPresent()) {
			User dbUser = recUser.get();
			dbUser.setEmail(user.getEmail());
			dbUser.setName(user.getName());
			dbUser.setPassword(user.getPassword());
			dbUser.setPhone(user.getPhone());
			dbUser.setAge(user.getAge());
			structure.setData(userDao.registerUser(dbUser));
			structure.setMessage("User Updated");
			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<ResponseStructure<User>>(structure, HttpStatus.ACCEPTED);
		}
		throw new UserNotFoundException("InValid User Id");
	}

	public ResponseEntity<ResponseStructure<User>> verify(String email, String password) {
		ResponseStructure<User> structure = new ResponseStructure<>();
		Optional<User> verifyUser = userDao.verifyUser(email, password);

		if (verifyUser.isPresent()) {
			structure.setData(verifyUser.get());
			structure.setMessage("Login Successfull");
			structure.setStatusCode(HttpStatus.OK.value());

			return new ResponseEntity<ResponseStructure<User>>(structure, HttpStatus.OK);
		} else {
			throw new InvalidCredentialsException("Invalid Email or Password");
		}
	}
}
