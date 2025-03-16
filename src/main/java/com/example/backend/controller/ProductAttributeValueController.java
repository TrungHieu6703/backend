package com.example.backend.controller;

import com.example.backend.dto.request.ProductAttributeValueDTO;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.ProductAttributeValueRes;
import com.example.backend.service.ProductAttributeValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-attribute-values")
public class ProductAttributeValueController {

    @Autowired
    private ProductAttributeValueService productAttributeValueService;

    // Tạo mới ProductAttributeValue
    @PostMapping
    public ResponseEntity<ApiResponse<ProductAttributeValueRes>> createProductAttributeValue(@RequestBody ProductAttributeValueDTO productAttributeValueDTO) {
        ProductAttributeValueRes productAttributeValueRes = productAttributeValueService.createProductAttributeValue(productAttributeValueDTO);
        ApiResponse<ProductAttributeValueRes> response = new ApiResponse<>("Product attribute value created successfully", HttpStatus.CREATED.value(), productAttributeValueRes);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Cập nhật ProductAttributeValue theo ID
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductAttributeValueRes>> updateProductAttributeValue(@PathVariable String id, @RequestBody ProductAttributeValueDTO productAttributeValueDTO) {
        ProductAttributeValueRes updatedProductAttributeValue = productAttributeValueService.updateProductAttributeValue(id, productAttributeValueDTO);
        ApiResponse<ProductAttributeValueRes> response = new ApiResponse<>("Product attribute value updated successfully", HttpStatus.OK.value(), updatedProductAttributeValue);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Xóa ProductAttributeValue theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProductAttributeValue(@PathVariable String id) {
        productAttributeValueService.deleteProductAttributeValue(id);
        ApiResponse<Void> response = new ApiResponse<>("Product attribute value deleted successfully", HttpStatus.OK.value(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy ProductAttributeValue theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductAttributeValueRes>> getProductAttributeValueById(@PathVariable String id) {
        ProductAttributeValueRes productAttributeValueRes = productAttributeValueService.getProductAttributeValueById(id);
        ApiResponse<ProductAttributeValueRes> response = new ApiResponse<>("Product attribute value retrieved successfully", HttpStatus.OK.value(), productAttributeValueRes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy tất cả ProductAttributeValues
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductAttributeValueRes>>> getAllProductAttributeValues() {
        List<ProductAttributeValueRes> productAttributeValues = productAttributeValueService.getAllProductAttributeValues();
        ApiResponse<List<ProductAttributeValueRes>> response = new ApiResponse<>("All product attribute values retrieved successfully", HttpStatus.OK.value(), productAttributeValues);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
