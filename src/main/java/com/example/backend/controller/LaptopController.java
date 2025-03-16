package com.example.backend.controller;

import com.example.backend.entity.Brand;
import com.example.backend.entity.Product_line;
import com.example.backend.service.LaptopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}