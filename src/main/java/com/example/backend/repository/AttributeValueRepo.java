package com.example.backend.repository;

import com.example.backend.entity.AttributeValue;
import com.example.backend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttributeValueRepo extends JpaRepository<AttributeValue, String> {
    List<AttributeValue> findByAttribute_Id(String attribute_id);

    @Query("SELECT av FROM AttributeValue av WHERE av.attribute.id = :attributeId")
    List<AttributeValue> findByAttributeId(@Param("attributeId") String attributeId);


    @Query("SELECT COUNT(*) > 0 FROM ProductAttributeValue p WHERE p.attributeValue.id = :attributeValueId")
    boolean existsProductsByAttributeValueId(@Param("attributeValueId") String attributeValueId);

    @Query("SELECT b FROM AttributeValue b WHERE b.is_deleted = false")
    List<AttributeValue> findAllActiveAttributeValues();
}
