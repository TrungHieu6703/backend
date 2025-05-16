package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeResponseDTO {
    private String id;
    private String name;
    private String id_select;
    private String description;
    private List<AttributeOptionDTO> options;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttributeOptionDTO {
        private String id;
        private String value;
    }
}