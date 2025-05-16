package com.example.backend.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryAttributeRes {
    private String id;
    private String categoryId;
    private String attributeId;
    private boolean visible;
    private boolean display;
    private boolean filter;
}