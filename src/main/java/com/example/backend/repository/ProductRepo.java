package com.example.backend.repository;

import com.example.backend.entity.Category;
import com.example.backend.entity.Product;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, String> {
    List<Product> findByIdIn(List<String> ids);

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productAttributeValues pav LEFT JOIN FETCH pav.attributeValue av LEFT JOIN FETCH av.attribute a WHERE p.id IN :ids")
    List<Product> findAllByIdWithAttributes(@Param("ids") List<String> ids);

}
