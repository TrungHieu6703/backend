package com.example.backend.service;

import com.example.backend.dto.response.ProductResponseDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private BrandRepo brandRepo;

    @Autowired
    private AttributeRepo attributeRepo;

    @Autowired
    private AttributeValueRepo attributeValueRepo;

    public List<ProductResponseDTO> searchProducts(
            String keyword, String categoryId, String brandId,
            BigDecimal minPrice, BigDecimal maxPrice,
            List<String> attributeIds, List<String> attributeValues,
            int page, int size, String sortBy, String sortDirection) {

        // Tạo specification cho các điều kiện tìm kiếm
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Tìm theo từ khóa
            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + keyword.toLowerCase() + "%"));
            }

            // Tìm theo danh mục
            if (categoryId != null && !categoryId.isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        root.get("category").get("id"), categoryId));
            }

            // Tìm theo thương hiệu
            if (brandId != null && !brandId.isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        root.get("brand").get("id"), brandId));
            }

            // Lọc theo khoảng giá
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"), maxPrice));
            }

            // Lọc theo thuộc tính
            if (attributeIds != null && attributeValues != null && !attributeIds.isEmpty() && !attributeValues.isEmpty()) {
                for (int i = 0; i < attributeIds.size() && i < attributeValues.size(); i++) {
                    String attrId = attributeIds.get(i);
                    String attrValue = attributeValues.get(i);

                    Join<Product, ProductAttributeValue> productAttributeValueJoin = root.join("productAttributeValues");
                    Join<ProductAttributeValue, AttributeValue> attributeValueJoin = productAttributeValueJoin.join("attributeValue");
                    Join<AttributeValue, Attribute> attributeJoin = attributeValueJoin.join("attribute");

                    predicates.add(criteriaBuilder.and(
                            criteriaBuilder.equal(attributeJoin.get("id"), attrId),
                            criteriaBuilder.like(productAttributeValueJoin.get("value"), "%" + attrValue + "%")
                    ));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Tạo trang và sắp xếp
        Sort sort = sortDirection.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Thực hiện tìm kiếm
        Page<Product> products = productRepo.findAll(spec, pageable);

        // Chuyển đổi kết quả sang DTO
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProductResponseDTO convertToDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory() != null ? product.getCategory().getName() : null);
        dto.setBrand(product.getBrand() != null ? product.getBrand().getName() : null);
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());

        // Lấy ảnh đầu tiên từ danh sách ảnh
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            dto.setAvatar(product.getImages().get(0));
        }

        return dto;
    }

    public Map<String, Object> getAvailableFilters() {
        Map<String, Object> filters = new HashMap<>();

        // Lấy danh sách danh mục
        List<Map<String, Object>> categories = categoryRepo.findAll().stream()
                .map(category -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", category.getId());
                    map.put("name", category.getName());
                    return map;
                }).collect(Collectors.toList());

        // Lấy danh sách thương hiệu
        List<Map<String, Object>> brands = brandRepo.findAll().stream()
                .map(brand -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", brand.getId());
                    map.put("name", brand.getName());
                    return map;
                }).collect(Collectors.toList());

        // Lấy danh sách thuộc tính và giá trị
        List<Map<String, Object>> attributes = attributeRepo.findAll().stream()
                .map(attribute -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", attribute.getId());
                    map.put("name", attribute.getName());

                    List<Map<String, Object>> values = attributeValueRepo.findByAttribute_Id(attribute.getId())
                            .stream()
                            .map(value -> {
                                Map<String, Object> valueMap = new HashMap<>();
                                valueMap.put("id", value.getId());
                                valueMap.put("value", value.getValue());
                                return valueMap;
                            }).collect(Collectors.toList());

                    map.put("values", values);
                    return map;
                }).collect(Collectors.toList());

        // Lấy khoảng giá
        BigDecimal minPrice = productRepo.findAll().stream()
                .map(Product::getPrice)
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        BigDecimal maxPrice = productRepo.findAll().stream()
                .map(Product::getPrice)
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.valueOf(1000000000));

        Map<String, BigDecimal> priceRange = new HashMap<>();
        priceRange.put("min", minPrice);
        priceRange.put("max", maxPrice);

        filters.put("categories", categories);
        filters.put("brands", brands);
        filters.put("attributes", attributes);
        filters.put("priceRange", priceRange);

        return filters;
    }
}