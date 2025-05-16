package com.example.backend.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ProductRes {
    String id;

    String name;

    String category_id;

    String brand_id;

    String specs_summary;

    String product_line_id;

    BigDecimal price;

    Boolean is_hot;

    int quantity;

    List<String> image;

    String description;
}
