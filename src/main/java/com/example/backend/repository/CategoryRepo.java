package com.example.backend.repository;

import com.example.backend.entity.Brand;
import com.example.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, String> {
    @Query("SELECT COUNT(*) > 0 FROM Product p WHERE p.category.id = :categoryId")
    boolean existsProductsByCategoryId(@Param("categoryId") String categoryId);

    List<Category> findByParentId(String parentId);


    @Query("SELECT b FROM Category b WHERE b.is_deleted = false")
    List<Category> findAllActiveCategories();
}
