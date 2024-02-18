package org.jsp.merchantproductapp.dao;

import java.util.Optional;

import org.jsp.merchantproductapp.dto.User;
import org.jsp.merchantproductapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
	@Autowired
	private UserRepository userRepository;

	public User registerUser(User user) {
		return userRepository.save(user);
	}
	
	
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public boolean existsByPhone(long phone) {
		return userRepository.existsByPhone(phone);
	}
	
	public Optional<User> getUserById(int id) {
		return userRepository.findById(id);
	}
	
	public Optional<User> verifyUser(String email, String password){
		return userRepository.findByEmailAndPassword(email, password);
	}
}
