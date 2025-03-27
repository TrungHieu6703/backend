package com.example.backend.controller;

import com.example.backend.entity.Brand;
import com.example.backend.entity.Product;
import com.example.backend.entity.Product_line;
import com.example.backend.service.LaptopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laptops")
public class LaptopController {
    private final LaptopService laptopService;

    public LaptopController(LaptopService laptopService) {
        this.laptopService = laptopService;
    }

    @GetMapping("/brands")
    public List<Brand> getBrands() {
        return laptopService.getAllBrands();
    }

    @GetMapping("/brands/{brandId}/product-lines")
    public List<Product_line> getProductLinesByBrand(@PathVariable String brandId) {
        return laptopService.getProductLinesByBrand(brandId);
    }

    @GetMapping("/compare")
    public ResponseEntity<List<Product>> compareLaptops(@RequestParam List<String> ids) {
        if (ids.size() < 2 || ids.size() > 4) {
            return ResponseEntity.badRequest().body(null);
        }
        List<Product> laptops = laptopService.compareLaptops(ids);
        return ResponseEntity.ok(laptops);
    }
}