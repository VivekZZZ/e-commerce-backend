package org.jsp.merchantproductapp.service;

import java.util.List;
import java.util.Optional;

import org.jsp.merchantproductapp.dao.MerchantDao;
import org.jsp.merchantproductapp.dto.Merchant;
import org.jsp.merchantproductapp.dto.ResponseStructure;
import org.jsp.merchantproductapp.exception.InvalidCredentialsException;
import org.jsp.merchantproductapp.exception.MerchantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MerchantService {
	@Autowired
	private MerchantDao merchantDao;

	public ResponseEntity<ResponseStructure<Merchant>> saveMerchant(Merchant merchant) {
		ResponseStructure<Merchant> structure = new ResponseStructure<>();
		try {
	        // Check for duplicate data before attempting to save
	        validateUniqueConstraints(merchant);       
	        // If validation passes, save the merchant
	        structure.setData(merchantDao.saveMerchant(merchant));
	        structure.setMessage("Merchant Saved");
	        structure.setStatusCode(HttpStatus.CREATED.value());
	        return new ResponseEntity<>(structure, HttpStatus.CREATED);
	    } catch (DuplicateKeyException e) {	
	        // Handle duplicate data exception
	        structure.setMessage(e.getMessage());
	        structure.setStatusCode(HttpStatus.CONFLICT.value());
	        
	        return new ResponseEntity<>(structure, HttpStatus.CONFLICT);
	    }
	}

	private void validateUniqueConstraints(Merchant merchant) {
		// Check if a merchant with the given email already exists
		if (merchantDao.existsByEmail(merchant.getEmail())) {
			throw new DuplicateKeyException("Email Already Registered");
		}

		// Check if a merchant with the given phone number already exists
		if (merchantDao.existsByPhone(merchant.getPhone())) {
			throw new DuplicateKeyException("Phone Number Already Exists");
		}

		// Check if a merchant with the given GST number already exists
		if (merchantDao.existsByGstNo(merchant.getGst_no())) {
			throw new DuplicateKeyException("Duplicate GST number found.");
		}
	}
	
	public ResponseEntity<ResponseStructure<Merchant>> getMerchantById(int id) {
		ResponseStructure<Merchant> structure = new ResponseStructure<>();
		Optional<Merchant> merchantById = merchantDao.getMerchantById(id);
		if(merchantById.isPresent()) {
			structure.setData(merchantById.get());
			structure.setMessage("Merchant Found");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<Merchant>>(structure, HttpStatus.OK);
		}else {
			throw new MerchantNotFoundException("Invalid Id");
		}
	}

	public ResponseEntity<ResponseStructure<Merchant>> updateMerchant(Merchant merchant) {
		ResponseStructure<Merchant> structure = new ResponseStructure<>();
		Optional<Merchant> recMerchant = merchantDao.findById(merchant.getId());
		if (recMerchant.isPresent()) {
			Merchant dbMerchant = recMerchant.get();
			dbMerchant.setEmail(merchant.getEmail());
			dbMerchant.setName(merchant.getName());
			dbMerchant.setPassword(merchant.getPassword());
			dbMerchant.setPhone(merchant.getPhone());
			dbMerchant.setGst_no(merchant.getGst_no());
			structure.setData(merchantDao.saveMerchant(dbMerchant));
			structure.setMessage("Merchant Updated");
			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<ResponseStructure<Merchant>>(structure, HttpStatus.ACCEPTED);
		}
		throw new MerchantNotFoundException("InValid Merchant Id");
	}

	public ResponseEntity<ResponseStructure<List<Merchant>>> findAllMerchant() {
		ResponseStructure<List<Merchant>> structure = new ResponseStructure<>();
		List<Merchant> merchants = merchantDao.findAllMerchant();
		if (merchants.size() > 0) {
			structure.setData(merchants);
			structure.setMessage("List Of Merchants");
			structure.setStatusCode(HttpStatus.FOUND.value());

			return new ResponseEntity<ResponseStructure<List<Merchant>>>(structure, HttpStatus.FOUND);
		}
		throw new MerchantNotFoundException("No Merchant Available");
	}

	public ResponseEntity<ResponseStructure<Optional<Merchant>>> verifyMerchant(String email, String password) {
		ResponseStructure<Optional<Merchant>> structure = new ResponseStructure<>();
		Optional<Merchant> recMerchant = merchantDao.verify(email, password);
		if (recMerchant.isPresent()) {
			structure.setData(merchantDao.verify(email, password));
			structure.setMessage("Login Successful");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<Optional<Merchant>>>(structure, HttpStatus.OK);
		}
		throw new InvalidCredentialsException("Invalid email or password");
	}

	public ResponseEntity<ResponseStructure<String>> deleteMerchantById(int merchantId) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		boolean deleteById = merchantDao.deleteById(merchantId);
		if (deleteById) {
			structure.setData("Merchant Found");
			structure.setMessage("Merchant deleted Successfully");
			structure.setStatusCode(HttpStatus.FOUND.value());
			return new ResponseEntity<ResponseStructure<String>>(structure, HttpStatus.FOUND);
		}
		structure.setData("Merchant Not Found");
		structure.setMessage("Cannot Delete Merchant");
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ResponseStructure<String>>(structure, HttpStatus.NOT_FOUND);
	}
}
