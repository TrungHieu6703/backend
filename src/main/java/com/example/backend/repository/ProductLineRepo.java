package com.example.backend.repository;

import com.example.backend.entity.Product_line;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLineRepo extends JpaRepository<Product_line, String> {
}
