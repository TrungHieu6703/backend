// CategoryAttributeDTO.java
package com.example.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryAttributeDTO {
    private String categoryId;
    private String attributeId;
    private boolean visible;
    private boolean display;
    private boolean filter;
}