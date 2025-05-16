package com.example.backend.controller;

import com.example.backend.dto.request.ProductLineDTO;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.ProductLineRes;
import com.example.backend.service.ProductLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-lines")
public class ProductLineController {
    @Autowired
    private ProductLineService productLineService;

    // Tạo mới ProductLine
    @PostMapping
    public ResponseEntity<ApiResponse<ProductLineRes>> createProductLine(@RequestBody ProductLineDTO productLineDTO) {
        ProductLineRes productLineRes = productLineService.createProductLine(productLineDTO);
        ApiResponse<ProductLineRes> response = new ApiResponse<>("Attribute value created successfully", HttpStatus.CREATED.value(), productLineRes);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Cập nhật ProductLine theo ID
    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductLineRes>> updateProductLine(@PathVariable String id, @RequestBody ProductLineDTO productLineDTO) {
        ProductLineRes updatedProductLine = productLineService.updateProductLine(id, productLineDTO);
        ApiResponse<ProductLineRes> response = new ApiResponse<>("Attribute value updated successfully", HttpStatus.OK.value(), updatedProductLine);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Xóa ProductLine theo ID
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductLine(@PathVariable String id) {
        productLineService.deleteProductLine(id);
        return ResponseEntity.ok().body("Brand deleted successfully");
    }

    // Lấy ProductLine theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductLineRes>> getProductLineById(@PathVariable String id) {
        ProductLineRes productLineRes = productLineService.getProductLineById(id);
        ApiResponse<ProductLineRes> response = new ApiResponse<>("Attribute value retrieved successfully", HttpStatus.OK.value(), productLineRes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy tất cả ProductLines
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductLineRes>>> getAllProductLines() {
        List<ProductLineRes> productLines = productLineService.getAllProductLines();
        ApiResponse<List<ProductLineRes>> response = new ApiResponse<>("All attribute values retrieved successfully", HttpStatus.OK.value(), productLines);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
