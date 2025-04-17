package com.example.backend.repository;

import com.example.backend.entity.Attribute;
import com.example.backend.entity.CategoryAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryAttributeRepo extends JpaRepository<CategoryAttribute, String> {
    @Query("SELECT ca.attribute FROM CategoryAttribute ca WHERE ca.category.id = :categoryId")
    List<Attribute> findAttributesByCategoryId(@Param("categoryId") String categoryId);

    @Query("SELECT ca.attribute FROM CategoryAttribute ca WHERE ca.category.id = :categoryId AND ca.visible = true")
    List<Attribute> findVisibleAttributesByCategoryId(@Param("categoryId") String categoryId);
}
