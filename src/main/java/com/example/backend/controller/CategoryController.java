package com.example.backend.controller;

import com.example.backend.dto.request.CategoryDTO;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.CategoryRes;
import com.example.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Tạo mới Category
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryRes>> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryRes categoryRes = categoryService.createCategory(categoryDTO);
        ApiResponse<CategoryRes> response = new ApiResponse<>("Category created successfully", HttpStatus.CREATED.value(), categoryRes);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Cập nhật Category theo ID
    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryRes>> updateCategory(@PathVariable String id, @RequestBody CategoryDTO categoryDTO) {
        CategoryRes updatedCategory = categoryService.updateCategory(id, categoryDTO);
        ApiResponse<CategoryRes> response = new ApiResponse<>("Category updated successfully", HttpStatus.OK.value(), updatedCategory);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Xóa Category theo ID
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        ApiResponse<Void> response = new ApiResponse<>("Category deleted successfully", HttpStatus.OK.value(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy Category theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryRes>> getCategoryById(@PathVariable String id) {
        CategoryRes categoryRes = categoryService.getCategoryById(id);
        ApiResponse<CategoryRes> response = new ApiResponse<>("Category retrieved successfully", HttpStatus.OK.value(), categoryRes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy tất cả Category
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryRes>>> getAllCategories() {
        List<CategoryRes> categories = categoryService.getAllCategorys();
        ApiResponse<List<CategoryRes>> response = new ApiResponse<>("All categories retrieved successfully", HttpStatus.OK.value(), categories);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
