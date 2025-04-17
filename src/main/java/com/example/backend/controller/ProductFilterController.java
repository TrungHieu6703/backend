package com.example.backend.controller;

import com.example.backend.dto.request.ProductDTO;
import com.example.backend.dto.response.ProductFilterDTO;
import com.example.backend.service.ProductFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductFilterController {

    @Autowired
    private ProductFilterService productFilterService;

    @GetMapping("/products/productline/{productLineId}")
    public ResponseEntity<?> getProductsByProductLine(
            @PathVariable String productLineId,
            @RequestParam(required = false) Map<String, String> filters,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        List<ProductFilterDTO> products = productFilterService.getProductsByProductLine(
                productLineId, filters, page, size);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(
            @PathVariable String categoryId,
            @RequestParam(required = false) Map<String, String> filters,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        List<ProductFilterDTO> products = productFilterService.getProductsByCategory(
                categoryId, filters, page, size);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/brand/{brandId}")
    public ResponseEntity<?> getProductsByBrand(
            @PathVariable String brandId,
            @RequestParam(required = false) Map<String, String> filters,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        List<ProductFilterDTO> products = productFilterService.getProductsByBrand(
                brandId, filters, page, size);

        return ResponseEntity.ok(products);
    }

    // 4. API lấy các thuộc tính lọc theo danh mục
    @GetMapping("/filters/category/{categoryId}")
    public ResponseEntity<?> getFiltersByCategory(@PathVariable String categoryId) {
        Map<String, Object> filters = productFilterService.getFiltersByCategory(categoryId);
        return ResponseEntity.ok(filters);
    }
}
