package com.example.backend.repository;

import com.example.backend.entity.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributeValueRepo extends JpaRepository<AttributeValue, String> {
    List<AttributeValue> findByAttribute_Id(String attribute_id);
}
