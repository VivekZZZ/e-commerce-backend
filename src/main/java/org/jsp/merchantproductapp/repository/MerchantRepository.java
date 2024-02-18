package org.jsp.merchantproductapp.repository;

import java.util.Optional;

import org.jsp.merchantproductapp.dto.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MerchantRepository extends JpaRepository<Merchant, Integer> {
	@Query("select m from Merchant m where m.email=?1 and m.password=?2")
	Optional<Merchant> findByEmailAndPassword(String email, String password);

	boolean existsByEmail(String email);

	boolean existsByPhone(long phone);

	@Query("select m from Merchant m where m.gst_no=?1")
	Optional<Merchant> existsByGst_no(String gst_no);

}
