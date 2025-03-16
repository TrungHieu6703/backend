package com.example.backend.repository;

import com.example.backend.entity.Product_line;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductLineRepository extends JpaRepository<Product_line, String> {
    List<Product_line> findByBrand_Id(String brandId);
}
