package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;

@RestController
public class CategoryController {
	@Autowired
	CategoryRepository repo;
	
	@PostMapping("/category/save")
	public ResponseEntity<Category> createCategory(@RequestBody Category category) {
		
		try {
			Category _category = repo.save(category);
			return new ResponseEntity<>(_category, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/category/update/{id}")
	public ResponseEntity<Category> updateCategory(@PathVariable("id") int id, @RequestBody Category category) {
		Optional<Category> categoryData = repo.findById(id);

		if (categoryData.isPresent()) {
			Category _category = categoryData.get();
			_category.setName(category.getName());
			return new ResponseEntity<>(repo.save(_category), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	
	@GetMapping("/category/{id}")
	public ResponseEntity<Category> getCategoryById(@PathVariable("id") int id) {
		Optional<Category> categoryData = repo.findById(id);

		if (categoryData.isPresent()) {
			return new ResponseEntity<>(categoryData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/getallcategorys")
	public ResponseEntity<List<Category>> getAllCategorys() {
		try {
			List<Category> categorys = new ArrayList<Category>();

				repo.findAll().forEach(categorys::add);
			
			if (categorys.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(categorys, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/category/delete/{id}")
	public ResponseEntity<HttpStatus> deleteCategory(@PathVariable("id") int id) {
		try {
			repo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/deleteallcategorys")
	public ResponseEntity<HttpStatus> deleteAllCategorys() {
		try {
			repo.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	
}
