package org.jsp.merchantproductapp.repository;

import java.util.Optional;

import org.jsp.merchantproductapp.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

	boolean existsByEmail(String email);

	boolean existsByPhone(long phone);
	
	@Query("select u from User u where u.email=?1 and u.password = ?2")
	Optional<User> findByEmailAndPassword(String email, String password);

}
