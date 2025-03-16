package com.example.backend.service;

import com.example.backend.entity.Brand;
import com.example.backend.entity.Product_line;
import com.example.backend.repository.BrandRepo;
import com.example.backend.repository.ProductLineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaptopService {
    private final BrandRepo brandRepository;
    private final ProductLineRepository productLineRepository;

    public LaptopService(BrandRepo brandRepository, ProductLineRepository productLineRepository) {
        this.brandRepository = brandRepository;
        this.productLineRepository = productLineRepository;
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public List<Product_line> getProductLinesByBrand(String brandId) {
        return productLineRepository.findByBrand_Id(brandId);
    }
}