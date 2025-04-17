package com.example.backend.repository;

import com.example.backend.entity.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductAttributeValueRepo extends JpaRepository<ProductAttributeValue, String> {
    @Modifying
    @Query("DELETE FROM ProductAttributeValue pav WHERE pav.product.id = :productId")
    void deleteByProductId(@Param("productId") String productId);

    List<ProductAttributeValue> findByProductId(String productId);
}
