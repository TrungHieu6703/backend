package com.example.backend.service;

import com.example.backend.dto.response.CartResponse;
import com.example.backend.dto.response.ProductCompareDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class LaptopService {
    private final BrandRepo brandRepository;
    private final ProductLineRepository productLineRepository;
    private final ProductRepo productRepo;
    private final ProductAttributeValueRepo productAttributeValueRepo;
    private final AttributeValueRepo attributeValueRepo;
    private final AttributeRepo attributeRepo;

    public LaptopService(BrandRepo brandRepository,
                         ProductLineRepository productLineRepository,
                         ProductRepo productRepo,
                         ProductAttributeValueRepo productAttributeValueRepo,
                         AttributeValueRepo attributeValueRepo,
                         AttributeRepo attributeRepo
                         ) {
        this.brandRepository = brandRepository;
        this.productLineRepository = productLineRepository;
        this.productRepo = productRepo;
        this.productAttributeValueRepo = productAttributeValueRepo;
        this.attributeValueRepo = attributeValueRepo;
        this.attributeRepo = attributeRepo;
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public List<Product_line> getProductLinesByBrand(String brandId) {
        return productLineRepository.findByBrand_Id(brandId);
    }

    public List<Product> compareLaptops(List<String> laptopIds) {
        return productRepo.findByIdIn(laptopIds);
    }

    public List<CartResponse> getProductsFromCartCookie(String cartCookie) {
        if (cartCookie == null || cartCookie.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            // Giải mã URL
            String decodedCookie = java.net.URLDecoder.decode(cartCookie, "UTF-8");

            // Dùng Jackson để parse JSON array thành List<String>
            ObjectMapper mapper = new ObjectMapper();
            List<String> ids = mapper.readValue(decodedCookie, new TypeReference<List<String>>() {
            });

            List<Product> products = productRepo.findAllById(ids);

            List<CartResponse> responses = products.stream()
                    .map(product -> new CartResponse(
                            product.getId(),
                            product.getName(),
                            product.getImages().isEmpty() ? null : product.getImages().get(0),
                            product.getPrice()
                    ))
                    .collect(Collectors.toList());

            System.out.println("Final CartResponse list: " + responses);
            return responses;
        } catch (Exception e) {
            System.err.println("Error processing cart cookie: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ProductCompareDTO> compareProductsWithAttributes(List<String> productIds) {
        // Lấy danh sách sản phẩm cơ bản (không có thuộc tính)
        List<Product> products = productRepo.findAllById(productIds);
        List<ProductCompareDTO> result = new ArrayList<>();

        // Tạo map để lưu các thuộc tính của từng sản phẩm
        Map<String, Map<String, String>> productAttributesMap = new HashMap<>();

        // Lấy tất cả các thuộc tính của danh sách sản phẩm bằng truy vấn riêng
        for (String productId : productIds) {
            // Lấy danh sách ProductAttributeValue cho từng sản phẩm
            List<ProductAttributeValue> attributeValues = productAttributeValueRepo.findByProductId(productId);

            Map<String, String> attributes = new HashMap<>();

            for (ProductAttributeValue pav : attributeValues) {
                AttributeValue attrValue = attributeValueRepo.findById(pav.getAttributeValue().getId()).orElse(null);
                if (attrValue != null) {
                    Attribute attribute = attributeRepo.findById(attrValue.getAttribute().getId()).orElse(null);
                    if (attribute != null) {
                        attributes.put(attribute.getName(), pav.getValue());
                    }
                }
            }

            productAttributesMap.put(productId, attributes);
        }

        // Tạo các DTO cho từng sản phẩm
        for (Product product : products) {
            ProductCompareDTO dto = new ProductCompareDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setBrand(product.getBrand() != null ? product.getBrand().getName() : null);
            dto.setCategory(product.getCategory() != null ? product.getCategory().getName() : null);
            dto.setPrice(product.getPrice());
            dto.setImages(product.getImages());

            // Lấy thuộc tính từ map đã tạo
            dto.setAttributes(productAttributesMap.getOrDefault(product.getId(), new HashMap<>()));

            result.add(dto);
        }

        return result;
    }

    public ProductCompareDTO getProductWithAttributes(String productId) {
        // Lấy thông tin cơ bản của sản phẩm
        Product product = productRepo.findById(productId).orElse(null);

        if (product == null) {
            return null; // Không tìm thấy sản phẩm
        }

        ProductCompareDTO dto = new ProductCompareDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setBrand(product.getBrand() != null ? product.getBrand().getName() : null);
        dto.setCategory(product.getCategory() != null ? product.getCategory().getName() : null);
        dto.setPrice(product.getPrice());
        dto.setImages(product.getImages());
        dto.setQuantity(String.valueOf(product.getQuantity()));
        dto.setDescription(product.getDescription());
        // Lấy thuộc tính của sản phẩm
        Map<String, String> attributes = new HashMap<>();

        // Lấy danh sách ProductAttributeValue cho sản phẩm
        List<ProductAttributeValue> attributeValues = productAttributeValueRepo.findByProductId(productId);

        for (ProductAttributeValue pav : attributeValues) {
            AttributeValue attrValue = attributeValueRepo.findById(pav.getAttributeValue().getId()).orElse(null);
            if (attrValue != null) {
                Attribute attribute = attributeRepo.findById(attrValue.getAttribute().getId()).orElse(null);
                if (attribute != null) {
                    attributes.put(attribute.getName(), pav.getValue());
                }
            }
        }

        dto.setAttributes(attributes);

        return dto;
    }
}