package com.example.backend.repository;

import com.example.backend.entity.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeValueRepo extends JpaRepository<ProductAttributeValue, String> {
}
