// CategoryAttributeController.java
package com.example.backend.controller;

import com.example.backend.dto.request.CategoryAttributeDTO;
import com.example.backend.dto.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.service.CategoryAttributeService;

import java.util.List;

@RestController
@RequestMapping("/category-attributes")
public class CategoryAttributeController {

    @Autowired
    private CategoryAttributeService categoryAttributeService;

    // Lấy tất cả mối quan hệ giữa danh mục và thuộc tính
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryAttributeDTO>>> getAllCategoryAttributes() {
        List<CategoryAttributeDTO> list = categoryAttributeService.getAllCategoryAttributes();
        ApiResponse<List<CategoryAttributeDTO>> response =
                new ApiResponse<>("Category attributes retrieved successfully", HttpStatus.OK.value(), list);
        return ResponseEntity.ok(response);
    }

    // Lấy thuộc tính của một danh mục cụ thể
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<CategoryAttributeDTO>>> getCategoryAttributesByCategoryId(
            @PathVariable String categoryId) {
        List<CategoryAttributeDTO> list = categoryAttributeService.getCategoryAttributesByCategoryId(categoryId);
        ApiResponse<List<CategoryAttributeDTO>> response =
                new ApiResponse<>("Category attributes retrieved successfully", HttpStatus.OK.value(), list);
        return ResponseEntity.ok(response);
    }

    // Lưu một mối quan hệ
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryAttributeDTO>> saveCategoryAttribute(
            @RequestBody CategoryAttributeDTO categoryAttributeDTO) {
        CategoryAttributeDTO saved = categoryAttributeService.saveCategoryAttribute(categoryAttributeDTO);
        ApiResponse<CategoryAttributeDTO> response =
                new ApiResponse<>("Category attribute saved successfully", HttpStatus.OK.value(), saved);
        return ResponseEntity.ok(response);
    }

    // Lưu nhiều mối quan hệ cùng lúc
    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<List<CategoryAttributeDTO>>> saveBatchCategoryAttributes(
            @RequestBody List<CategoryAttributeDTO> categoryAttributes) {
        List<CategoryAttributeDTO> savedList = categoryAttributeService.saveBatchCategoryAttributes(categoryAttributes);
        ApiResponse<List<CategoryAttributeDTO>> response =
                new ApiResponse<>("Category attributes saved successfully", HttpStatus.OK.value(), savedList);
        return ResponseEntity.ok(response);
    }

    // Xóa một mối quan hệ
    @DeleteMapping("/{categoryId}/{attributeId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategoryAttribute(
            @PathVariable String categoryId,
            @PathVariable String attributeId) {
        categoryAttributeService.deleteCategoryAttribute(categoryId, attributeId);
        ApiResponse<Void> response =
                new ApiResponse<>("Category attribute deleted successfully", HttpStatus.OK.value(), null);
        return ResponseEntity.ok(response);
    }
}