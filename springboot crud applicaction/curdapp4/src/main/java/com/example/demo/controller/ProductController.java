package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;



@RestController
public class ProductController {
	@Autowired
	ProductRepository repo;
	
	@PostMapping("/product/save")
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		
		try {
			Product _product = repo.save(product);
			return new ResponseEntity<>(_product, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/product/update/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable("id") long id, @RequestBody Product product) {
		Optional<Product> productData = repo.findById(id);

		if (productData.isPresent()) {
			Product _product = productData.get();
			_product.setName(product.getName());
			_product.setPrice(product.getPrice());
			_product.setCategory(product.getCategory());
			return new ResponseEntity<>(repo.save(_product), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	
	@GetMapping("/product/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") long id) {
		Optional<Product> productData = repo.findById(id);

		if (productData.isPresent()) {
			return new ResponseEntity<>(productData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	

@GetMapping("/products")
	  public ResponseEntity<Map<String, Object>> getAllProducts(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "3") int size
	      ) {

	    try {
	      List<Product> products = new ArrayList<Product>();
	      Pageable paging = PageRequest.of(page, size);
	      
	      Page<Product> pages;
	        pages = repo.findAll(paging);
	      products = pages.getContent();

	      Map<String, Object> response = new HashMap<>();
	      response.put("products", products);
	      response.put("currentPage", pages.getNumber());
	      response.put("totalItems", pages.getTotalElements());
	      response.put("totalPages", pages.getTotalPages());

	      return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (Exception e) {
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
	@DeleteMapping("/product/delete/{id}")
	public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") long id) {
		try {
			repo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/deleteallproducts")
	public ResponseEntity<HttpStatus> deleteAllProducts() {
		try {
			repo.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
