package com.example.backend.repository;

import com.example.backend.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepo extends JpaRepository<Attribute, String> {
}
