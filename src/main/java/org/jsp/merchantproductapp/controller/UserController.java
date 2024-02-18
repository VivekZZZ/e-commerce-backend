package org.jsp.merchantproductapp.controller;

import org.jsp.merchantproductapp.dto.ResponseStructure;
import org.jsp.merchantproductapp.dto.User;
import org.jsp.merchantproductapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/")
	public ResponseEntity<ResponseStructure<User>> registerUser(@RequestBody User user) {
		 return userService.registerUser(user);
	}

	@PutMapping("/")
	public ResponseEntity<ResponseStructure<User>> updateUser(@RequestBody User user) {
		return userService.updateUser(user);
	}
	
	@PostMapping("/verify-by-email")
	public ResponseEntity<ResponseStructure<User>> VerifyUser(@RequestParam String email, @RequestParam String password) {
		return userService.verify(email, password);
	}
	
}
