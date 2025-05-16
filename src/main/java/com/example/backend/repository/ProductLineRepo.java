package com.example.backend.repository;

import com.example.backend.entity.AttributeValue;
import com.example.backend.entity.Brand;
import com.example.backend.entity.Product_line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductLineRepo extends JpaRepository<Product_line, String> {
    @Query("SELECT COUNT(*) > 0 FROM Product p WHERE p.product_line.id = :product_lineId")
    boolean existsProductsByProductLineId(@Param("product_lineId") String product_lineId);

    @Query("SELECT b FROM Product_line b WHERE b.is_deleted = false")
    List<Product_line> findAllActiveProductLines();
}
