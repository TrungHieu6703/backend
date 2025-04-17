// ProductCompareDTO.java
package com.example.backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ProductCompareDTO {
    private String id;
    private String name;
    private String brand;
    private String quantity;
    private String description;
    private String category;
    private BigDecimal price;
    private List<String> images;
    private Map<String, String> attributes; // Tên thuộc tính -> giá trị thuộc tính
}