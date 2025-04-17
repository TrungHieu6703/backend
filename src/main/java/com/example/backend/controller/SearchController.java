package com.example.backend.controller;

import com.example.backend.dto.response.ProductResponseDTO;
import com.example.backend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String brandId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) List<String> attributeIds,
            @RequestParam(required = false) List<String> attributeValues,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        List<ProductResponseDTO> products = searchService.searchProducts(
                keyword, categoryId, brandId, minPrice, maxPrice,
                attributeIds, attributeValues, page, size, sortBy, sortDirection);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/filters")
    public ResponseEntity<Map<String, Object>> getAvailableFilters() {
        Map<String, Object> filters = searchService.getAvailableFilters();
        return ResponseEntity.ok(filters);
    }
}