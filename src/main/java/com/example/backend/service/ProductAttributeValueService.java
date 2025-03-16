package com.example.backend.service;

import com.example.backend.dto.request.ProductAttributeValueDTO;
import com.example.backend.dto.response.ProductAttributeValueRes;
import com.example.backend.entity.AttributeValue;
import com.example.backend.entity.Product;
import com.example.backend.entity.ProductAttributeValue;
import com.example.backend.repository.AttributeValueRepo;
import com.example.backend.repository.ProductAttributeValueRepo;
import com.example.backend.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductAttributeValueService {
    @Autowired
    private ProductAttributeValueRepo productAttributeValueRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private AttributeValueRepo attributeValueRepo;

    public ProductAttributeValueRes createProductAttributeValue(ProductAttributeValueDTO productAttributeValueDTO) {
        Product product = productRepo.findById(productAttributeValueDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        AttributeValue attributeValue = attributeValueRepo.findById(productAttributeValueDTO.getAttributeValueId())
                .orElseThrow(() -> new RuntimeException("AttributeValue not found"));

        ProductAttributeValue productAttributeValue = new ProductAttributeValue();
        productAttributeValue.setProduct(product);
        productAttributeValue.setAttributeValue(attributeValue);
        productAttributeValue.setValue(productAttributeValueDTO.getValue());

        ProductAttributeValue savedProductAttributeValue = productAttributeValueRepo.save(productAttributeValue);

        return new ProductAttributeValueRes(savedProductAttributeValue.getId(),
                product.getId(), attributeValue.getId(), savedProductAttributeValue.getValue());
    }

    public ProductAttributeValueRes updateProductAttributeValue(String id, ProductAttributeValueDTO productAttributeValueDTO) {
        ProductAttributeValue productAttributeValue = productAttributeValueRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductAttributeValue not found"));

        Product product = productRepo.findById(productAttributeValueDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        AttributeValue attributeValue = attributeValueRepo.findById(productAttributeValueDTO.getAttributeValueId())
                .orElseThrow(() -> new RuntimeException("AttributeValue not found"));

        productAttributeValue.setProduct(product);
        productAttributeValue.setAttributeValue(attributeValue);
        productAttributeValue.setValue(productAttributeValueDTO.getValue());

        ProductAttributeValue updatedProductAttributeValue = productAttributeValueRepo.save(productAttributeValue);

        return new ProductAttributeValueRes(updatedProductAttributeValue.getId(),
                product.getId(), attributeValue.getId(), updatedProductAttributeValue.getValue());
    }

    public void deleteProductAttributeValue(String id) {
        ProductAttributeValue productAttributeValue = productAttributeValueRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductAttributeValue not found"));
        productAttributeValueRepo.delete(productAttributeValue);
    }

    public ProductAttributeValueRes getProductAttributeValueById(String id) {
        ProductAttributeValue productAttributeValue = productAttributeValueRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductAttributeValue not found"));
        return new ProductAttributeValueRes(productAttributeValue.getId(),
                productAttributeValue.getProduct().getId(),
                productAttributeValue.getAttributeValue().getId(),
                productAttributeValue.getValue());
    }

    public List<ProductAttributeValueRes> getAllProductAttributeValues() {
        List<ProductAttributeValue> productAttributeValues = productAttributeValueRepo.findAll();
        return productAttributeValues.stream()
                .map(productAttributeValue -> new ProductAttributeValueRes(productAttributeValue.getId(),
                        productAttributeValue.getProduct().getId(),
                        productAttributeValue.getAttributeValue().getId(),
                        productAttributeValue.getValue()))
                .collect(Collectors.toList());
    }
}
