package com.example.backend.service;

import com.example.backend.dto.request.ProductAttributeValueDTO;
import com.example.backend.dto.response.ProductAttributeResponseDTO;
import com.example.backend.dto.response.ProductAttributeValueRes;
import com.example.backend.entity.Attribute;
import com.example.backend.entity.AttributeValue;
import com.example.backend.entity.Product;
import com.example.backend.entity.ProductAttributeValue;
import com.example.backend.repository.AttributeRepo;
import com.example.backend.repository.AttributeValueRepo;
import com.example.backend.repository.ProductAttributeValueRepo;
import com.example.backend.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductAttributeValueService {
    @Autowired
    private ProductAttributeValueRepo productAttributeValueRepo;

    @Autowired
    private AttributeRepo attributeRepo;

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

    public List<ProductAttributeResponseDTO> getProductAttributes(String productId) {
        // Lấy thông tin sản phẩm
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + productId));

        // Lấy danh sách thuộc tính của danh mục sản phẩm này
        List<Attribute> categoryAttributes = attributeRepo.findAttributesByCategoryId(product.getCategory().getId());

        // Lấy giá trị thuộc tính của sản phẩm
        List<ProductAttributeValue> productAttributeValues = productAttributeValueRepo.findByProductId(productId);

        // Map lưu giá trị thuộc tính đã chọn của sản phẩm (attributeId -> attributeValueId)
        Map<String, String> selectedAttributeValueIds = new HashMap<>();

        // Map lưu mô tả chi tiết của thuộc tính cho sản phẩm (attributeId -> description)
        Map<String, String> attributeDescriptions = new HashMap<>();

        for (ProductAttributeValue pav : productAttributeValues) {
            AttributeValue attrValue = pav.getAttributeValue();
            if (attrValue != null) {
                String attributeId = attrValue.getAttribute().getId();
                selectedAttributeValueIds.put(attributeId, attrValue.getId());

                // Lưu lại giá trị (value) từ ProductAttributeValue làm description
                attributeDescriptions.put(attributeId, pav.getValue());
            }
        }

        // Biến đổi dữ liệu thành DTO
        List<ProductAttributeResponseDTO> result = new ArrayList<>();

        for (Attribute attribute : categoryAttributes) {
            String attributeId = attribute.getId();

            ProductAttributeResponseDTO attributeDTO = new ProductAttributeResponseDTO();
            attributeDTO.setId(attributeId);
            attributeDTO.setName(attribute.getName());

            // Lấy giá trị đã chọn (nếu có)
            attributeDTO.setId_select(selectedAttributeValueIds.getOrDefault(attributeId, null));

            // Sử dụng giá trị từ ProductAttributeValue làm description
            String description = attributeDescriptions.getOrDefault(attributeId, "");
            attributeDTO.setDescription(description);

            // Lấy các giá trị thuộc tính
            List<AttributeValue> attributeValues = attributeValueRepo.findByAttributeId(attributeId);

            List<ProductAttributeResponseDTO.AttributeOptionDTO> options = attributeValues.stream()
                    .map(av -> new ProductAttributeResponseDTO.AttributeOptionDTO(av.getId(), av.getValue()))
                    .collect(Collectors.toList());

            attributeDTO.setOptions(options);
            result.add(attributeDTO);
        }

        return result;
    }
}
