package org.jsp.merchantproductapp.controller;

import java.util.List;
import java.util.Optional;

import org.jsp.merchantproductapp.dto.Merchant;
import org.jsp.merchantproductapp.dto.ResponseStructure;
import org.jsp.merchantproductapp.service.MerchantService;
import org.jsp.merchantproductapp.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/merchants")
public class MerchantController {
	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private EmailUtil emailUtil;

	@PostMapping("/")
	public ResponseEntity<ResponseStructure<Merchant>> saveMerchant(@RequestBody Merchant merchant) {
		return merchantService.saveMerchant(merchant);
	}

	@PutMapping("/")
	public ResponseEntity<ResponseStructure<Merchant>> updateMerchant(@RequestBody Merchant merchant) {
		return merchantService.updateMerchant(merchant);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Merchant>> getOneMerchant(@PathVariable int id) {
		return merchantService.getMerchantById(id);
	}

	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<Merchant>>> findAllProducts() {
		return merchantService.findAllMerchant();
	}

	@PostMapping("/verify-by-email")
	public ResponseEntity<ResponseStructure<Optional<Merchant>>> verifyAdminByEmail(@RequestParam String email,
			@RequestParam String password) {
		return merchantService.verifyMerchant(email, password);
	}

	@DeleteMapping("/delete-by-merchnat-id/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteMerchantById(@PathVariable int id) {
		return merchantService.deleteMerchantById(id);
	}

	@PostMapping("/email")
	public String sendMail() {
		emailUtil.sendSimpleMessage("kumar3198vivek@gmail.com", "registration successfull", "congratutalions");
		return "email sent successfully";
	}

}
