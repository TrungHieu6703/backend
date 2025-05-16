package com.example.backend.repository;

import com.example.backend.entity.Attribute;
import com.example.backend.entity.CategoryAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryAttributeRepo extends JpaRepository<CategoryAttribute, String> {
    @Query("SELECT ca.attribute FROM CategoryAttribute ca WHERE ca.category.id = :categoryId AND ca.visible = true AND ca.display = true")
    List<Attribute> findAttributesByCategoryId(@Param("categoryId") String categoryId);

    @Query("SELECT ca.attribute FROM CategoryAttribute ca WHERE ca.category.id = :categoryId AND ca.visible = true")
    List<Attribute> findVisibleAttributesByCategoryId(@Param("categoryId") String categoryId);

    List<CategoryAttribute> findByCategoryId(String categoryId);

    @Query("SELECT ca FROM CategoryAttribute ca WHERE ca.category.id = :categoryId AND ca.attribute.id = :attributeId")
    Optional<CategoryAttribute> findByCategoryIdAndAttributeId(
            @Param("categoryId") String categoryId,
            @Param("attributeId") String attributeId
    );

    void deleteByCategoryIdAndAttributeId(String categoryId, String attributeId);
}
