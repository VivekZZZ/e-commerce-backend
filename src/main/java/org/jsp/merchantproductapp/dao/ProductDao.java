package org.jsp.merchantproductapp.dao;

import org.jsp.merchantproductapp.repository.MerchantRepository;
import org.jsp.merchantproductapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.jsp.merchantproductapp.dto.Merchant;
import org.jsp.merchantproductapp.dto.Product;

@Repository
public class ProductDao {
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private MerchantRepository merchantRepository;

	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	public Optional<Product> findById(int id) {
		return productRepository.findById(id);
	}

	public List<Product> findByBrand(String brand) {
		return productRepository.findByBrand(brand);
	}

	public List<Product> findByCategory(String category) {
		return productRepository.findByCategory(category);
	}

	public List<Product> findByMerchantId(int id) {
		return productRepository.findByMerchantId(id);
	}
	
	public boolean deleteByProductId(int productId) {
		// Retrieve the product by ID
		Product product = productRepository.findById(productId).orElse(null);
		if (product != null) {
			// retrieve the associated merchant
			Merchant merchant = product.getMerchant();

			// remove the product from merchant list
			merchant.getProducts().remove(product);
	        merchantRepository.save(merchant);
	        
	     // Delete the product
	        productRepository.delete(product);
			return true;
		}
		return false;
	}
	
	public List<Product> getAll(){
		List<Product> findAll = productRepository.findAll();
		return findAll;
	}

}
