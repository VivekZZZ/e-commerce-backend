package org.jsp.merchantproductapp.service;

import java.util.List;
import java.util.Optional;

import org.jsp.merchantproductapp.dao.MerchantDao;
import org.jsp.merchantproductapp.dao.ProductDao;
import org.jsp.merchantproductapp.dto.Merchant;
import org.jsp.merchantproductapp.dto.Product;
import org.jsp.merchantproductapp.dto.ResponseStructure;
import org.jsp.merchantproductapp.exception.MerchantNotFoundException;
import org.jsp.merchantproductapp.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private MerchantDao merchantDao;

	public ResponseEntity<ResponseStructure<Product>> saveProduct(Product product, int merchant_id) {
		ResponseStructure<Product> structure = new ResponseStructure<>();
		Optional<Merchant> dBMerchant = merchantDao.findById(merchant_id);
		if (dBMerchant.isPresent()) {
			Merchant merchant = dBMerchant.get();
			merchant.getProducts().add(product);
			product.setMerchant(merchant);
			merchantDao.saveMerchant(merchant);
			structure.setData(productDao.saveProduct(product));
			structure.setMessage("Product added");
			structure.setStatusCode(HttpStatus.CREATED.value());
			return new ResponseEntity<ResponseStructure<Product>>(structure, HttpStatus.CREATED);
		}
		throw new MerchantNotFoundException("Invalid Merchant id");
	}

	public ResponseEntity<ResponseStructure<Product>> updateProduct(Product product) {
		ResponseStructure<Product> structure = new ResponseStructure<>();
		Optional<Product> recProduct = productDao.findById(product.getId());
		if (recProduct.isPresent()) {
			Product dbProduct = recProduct.get();
			dbProduct.setBrand(product.getBrand());
			dbProduct.setCategory(product.getCategory());
			dbProduct.setDescription(product.getDescription());
			dbProduct.setCost(product.getCost());
			dbProduct.setDiscount_cost(product.getDiscount_cost());
			dbProduct.setQuantity(product.getQuantity());
			dbProduct.setColor(product.getColor());
			dbProduct.setImage_url(product.getImage_url());
			dbProduct.setName(product.getName());
			structure.setData(productDao.saveProduct(dbProduct));
			structure.setMessage("Product Updated");
			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<ResponseStructure<Product>>(structure, HttpStatus.ACCEPTED);
		}
		throw new ProductNotFoundException("Invalid Id");
	}

	public ResponseEntity<ResponseStructure<Product>> getProductById(int id) {
		ResponseStructure<Product> structure = new ResponseStructure<>();
		Optional<Product> productById = productDao.findById(id);
		if (productById.isPresent()) {
			structure.setData(productById.get());
			structure.setMessage("Product Found");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<Product>>(structure, HttpStatus.OK);
		} else {
			throw new ProductNotFoundException("Invalid Id");
		}
	}

	public ResponseEntity<ResponseStructure<List<Product>>> findByBrand(String brand) {
		ResponseStructure<List<Product>> structure = new ResponseStructure<>();
		List<Product> products = productDao.findByBrand(brand);
		if (products.size() > 0) {
			structure.setData(products);
			structure.setMessage("Products Found");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<List<Product>>>(structure, HttpStatus.OK);
		}
		throw new ProductNotFoundException("Invalid Brand");
	}

	public ResponseEntity<ResponseStructure<List<Product>>> findByCategory(String category) {
		ResponseStructure<List<Product>> structure = new ResponseStructure<>();
		List<Product> products = productDao.findByCategory(category);
		if (products.size() > 0) {
			structure.setData(products);
			structure.setMessage("Products Found");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<List<Product>>>(structure, HttpStatus.OK);
		}
		throw new ProductNotFoundException("Invalid Catgeory");
	}

	public ResponseEntity<ResponseStructure<List<Product>>> findByMerchantId(int id) {
		ResponseStructure<List<Product>> structure = new ResponseStructure<>();
		List<Product> products = productDao.findByMerchantId(id);
		if (products.size() > 0) {
			structure.setData(products);
			structure.setMessage("Products Found");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<List<Product>>>(structure, HttpStatus.OK);
		}
		throw new ProductNotFoundException("Invalid Merchant Id");
	}

	public ResponseEntity<ResponseStructure<String>> deleteProductById(int productId) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		boolean deleteById = productDao.deleteByProductId(productId);
		if (deleteById) {
			structure.setData("Product Found");
			structure.setMessage("Product Deleted Successfully");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<String>>(structure, HttpStatus.OK);
		}
		structure.setData("Product Not Found");
		structure.setMessage("Cannot Delete Product");
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ResponseStructure<String>>(structure, HttpStatus.NOT_FOUND);
	}

	public ResponseEntity<ResponseStructure<List<Product>>> getAllProducts() {
		ResponseStructure<List<Product>> structure = new ResponseStructure<>();
		List<Product> allProducts = productDao.getAll();

		if (allProducts.size() > 0) {
			structure.setData(allProducts);
			structure.setMessage("products Found");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<List<Product>>>(structure, HttpStatus.OK);
		} else {
			throw new ProductNotFoundException("No Products Available");
		}
	}
}
