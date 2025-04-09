package com.example.backend.service;

import com.example.backend.dto.response.CartResponse;
import com.example.backend.entity.Brand;
import com.example.backend.entity.Product;
import com.example.backend.entity.Product_line;
import com.example.backend.repository.BrandRepo;
import com.example.backend.repository.ProductLineRepository;
import com.example.backend.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class LaptopService {
    private final BrandRepo brandRepository;
    private final ProductLineRepository productLineRepository;
    private final ProductRepo productRepo;

    public LaptopService(BrandRepo brandRepository, ProductLineRepository productLineRepository, ProductRepo productRepo) {
        this.brandRepository = brandRepository;
        this.productLineRepository = productLineRepository;
        this.productRepo = productRepo;
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public List<Product_line> getProductLinesByBrand(String brandId) {
        return productLineRepository.findByBrand_Id(brandId);
    }

    public List<Product> compareLaptops(List<String> laptopIds) {
        return productRepo.findByIdIn(laptopIds);
    }

    public List<CartResponse> getProductsFromCartCookie(String cartCookie) {
        if (cartCookie == null || cartCookie.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            // Giải mã URL
            String decodedCookie = java.net.URLDecoder.decode(cartCookie, "UTF-8");

            // Dùng Jackson để parse JSON array thành List<String>
            ObjectMapper mapper = new ObjectMapper();
            List<String> ids = mapper.readValue(decodedCookie, new TypeReference<List<String>>() {
            });

            List<Product> products = productRepo.findAllById(ids);

            List<CartResponse> responses = products.stream()
                    .map(product -> new CartResponse(
                            product.getId(),
                            product.getName(),
                            product.getImages().isEmpty() ? null : product.getImages().get(0),
                            product.getPrice()
                    ))
                    .collect(Collectors.toList());

            System.out.println("Final CartResponse list: " + responses);
            return responses;
        } catch (Exception e) {
            System.err.println("Error processing cart cookie: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}