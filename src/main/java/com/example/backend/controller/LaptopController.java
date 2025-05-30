package com.example.backend.controller;

import com.example.backend.dto.response.CartResponse;
import com.example.backend.dto.response.ProductCompareDTO;
import com.example.backend.entity.Brand;
import com.example.backend.entity.Product;
import com.example.backend.entity.Product_line;
import com.example.backend.service.LaptopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    // LaptopController.java (phần compare)
    @GetMapping("/compare")
    public ResponseEntity<List<ProductCompareDTO>> compareLaptops(@RequestParam List<String> ids) {
        if (ids.size() < 2 || ids.size() > 4) {
            return ResponseEntity.badRequest().body(null);
        }
        List<ProductCompareDTO> compareResult = laptopService.compareProductsWithAttributes(ids);
        return ResponseEntity.ok(compareResult);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductCompareDTO> getProductWithAttributes(@PathVariable String id) {
        ProductCompareDTO productDetail = laptopService.getProductWithAttributes(id);
        if (productDetail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDetail);
    }

    @GetMapping("/items")
    public List<CartResponse> getCartItems(@CookieValue(value = "cart", defaultValue = "") String cartCookie) {
        System.out.println("Cookie value: " + cartCookie);
        return laptopService.getProductsFromCartCookie(cartCookie);
    }
}