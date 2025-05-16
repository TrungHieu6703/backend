package com.example.backend.repository;

import com.example.backend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandRepo extends JpaRepository<Brand, String>{
    @Query(value = """
    SELECT b.id AS brand_id, b.name AS brand_name,
           pl.id AS product_line_id, pl.line_name AS product_line_name
    FROM brand b
    LEFT JOIN product_line pl ON b.id = pl.brand_id
    """, nativeQuery = true)
    List<Object[]> findAllWithProductLinesNative();

    @Query("SELECT COUNT(*) > 0 FROM Product p WHERE p.brand.id = :brandId")
    boolean existsProductsByBrandId(@Param("brandId") String brandId);

    @Query("SELECT b FROM Brand b WHERE b.is_deleted = false")
    List<Brand> findAllActiveBrands();
}
