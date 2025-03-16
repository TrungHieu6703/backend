package com.example.backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductResponseDTO {
    String id;
    String name;
    String category;
    String brand;
    BigDecimal price;
    int quantity;
    String avatar;
}

