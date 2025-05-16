package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "product_line")
public class Product_line {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String line_name;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    @JsonIgnore
    Brand brand;

    @Column(name = "is_deleted", nullable = false)
    boolean is_deleted = false;

    @OneToMany(mappedBy = "product_line")
    Set<Product> products = new HashSet<>();
}
