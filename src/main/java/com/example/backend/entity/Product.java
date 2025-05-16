package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore
    Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    Brand brand;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    Coupon coupon;

    BigDecimal price;

    int quantity;

    Boolean is_hot;

    @Column(columnDefinition = "Text")
    String specs_summary;

    @ElementCollection
    List<String> images = new ArrayList<>();

    @Column(columnDefinition = "Text")
    String description;

    @OneToMany(mappedBy = "product")
    Set<ProductAttributeValue> productAttributeValues =new HashSet<>();

    @OneToMany(mappedBy = "product")
    Set<OrderDetail> orderDetails =new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "product_line_id")
    Product_line product_line;
}
