package com.example.backend.repository;

import com.example.backend.entity.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeValueRepo extends JpaRepository<AttributeValue, String> {
}
