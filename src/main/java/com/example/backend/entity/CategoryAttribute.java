package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "category_attribute")
public class CategoryAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "attribute_id")
    Attribute attribute;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @Column(name = "visible", nullable = false)
    boolean visible = true;

    @Column(name = "display", nullable = false)
    boolean display = false;

    @Column(name = "filter", nullable = false)
    boolean filter = false;
}
