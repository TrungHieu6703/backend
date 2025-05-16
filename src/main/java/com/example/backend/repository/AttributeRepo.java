package com.example.backend.repository;

import com.example.backend.entity.Attribute;
import com.example.backend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttributeRepo extends JpaRepository<Attribute, String> {
    @Query("SELECT DISTINCT a FROM Attribute a " +
            "JOIN CategoryAttribute ca ON a.id = ca.attribute.id " +
            "WHERE ca.category.id = :categoryId AND ca.visible = true")
    List<Attribute> findAttributesByCategoryId(@Param("categoryId") String categoryId);

    @Query("SELECT COUNT(*) > 0 FROM AttributeValue p WHERE p.attribute.id = :attributeId")
    boolean existsProductsByAttributeId(@Param("attributeId") String attributeId);

    @Query("SELECT b FROM Attribute b WHERE b.is_deleted = false")
    List<Attribute> findAllActiveAttributes();
}

