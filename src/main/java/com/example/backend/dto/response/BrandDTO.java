package com.example.backend.dto.response;

import java.util.List;


public class BrandDTO {
    private String name;
    private List<String> productLines;

    public BrandDTO(String name, List<String> productLines) {
        this.name = name;
        this.productLines = productLines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getProductLines() {
        return productLines;
    }

    public void setProductLines(List<String> productLines) {
        this.productLines = productLines;
    }
}

