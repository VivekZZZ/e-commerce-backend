package org.jsp.merchantproductapp.dao;

import java.util.List;
import java.util.Optional;

import org.jsp.merchantproductapp.dto.Merchant;
import org.jsp.merchantproductapp.repository.MerchantRepository;
import org.jsp.merchantproductapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MerchantDao {
	@Autowired
	private MerchantRepository merchantRepository;
	@Autowired
	private ProductRepository productRepository;

	public Merchant saveMerchant(Merchant merchant) {
		return merchantRepository.save(merchant);
	}

	public Optional<Merchant> findById(int id) {
		return merchantRepository.findById(id);
	}
	
	public List<Merchant> findAllMerchant(){
		return merchantRepository.findAll();
	}

	public Optional<Merchant> verify(String email, String password) {
		return merchantRepository.findByEmailAndPassword(email, password);
	}
	
	public Optional<Merchant> getMerchantById(int id) {
		return merchantRepository.findById(id);
	}

	public boolean deleteById(int merchantId) {
		// Retrieve the merchant by ID
		Merchant merchant = merchantRepository.findById(merchantId).orElse(null);
		if (merchant != null) {
			// Delete all associated products (optional)
			productRepository.deleteAll(merchant.getProducts());

			// delete the merchant
			merchantRepository.delete(merchant);
			return true;
		}
		return false;
	}

	public boolean existsByEmail(String email) {
		return merchantRepository.existsByEmail(email);
	}

	public boolean existsByPhone(long phone) {
		// TODO Auto-generated method stub
		return merchantRepository.existsByPhone(phone);
	}

	public boolean existsByGstNo(String gst_no) {
		// TODO Auto-generated method stub
		Optional<Merchant> existsByGst_no = merchantRepository.existsByGst_no(gst_no);
		if(existsByGst_no.isPresent()) {
			return true;
		}else {
			return false;
		}
	}
}
