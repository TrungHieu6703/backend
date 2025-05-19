package com.example.backend.service;

import com.example.backend.dto.request.ProductDTO;
import com.example.backend.dto.response.ProductFilterDTO;
import com.example.backend.entity.Attribute;
import com.example.backend.entity.AttributeValue;
import com.example.backend.entity.Product;
import com.example.backend.entity.ProductAttributeValue;
import com.example.backend.entity.Category;
import com.example.backend.repository.AttributeValueRepo;
import com.example.backend.repository.CategoryAttributeRepo;
import com.example.backend.repository.ProductRepo;
import com.example.backend.repository.CategoryRepo;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductFilterService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryAttributeRepo categoryAttributeRepo;

    @Autowired
    private AttributeValueRepo attributeValueRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    public List<ProductFilterDTO> getProductsByProductLine(
            String productLineId,
            Map<String, String> filters,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        // Tạo specification để lọc sản phẩm
        Specification<Product> spec = createFilterSpecification(null, null, productLineId, filters);

        Page<Product> productPage = productRepo.findAll(spec, pageable);

        // Chuyển đổi thành DTO và trả về danh sách
        return productPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductFilterDTO> getProductsByCategory(
            String categoryId,
            Map<String, String> filters,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        // Lấy danh sách các ID danh mục (bao gồm danh mục hiện tại và các danh mục con nếu có)
        List<String> categoryIds = getCategoryIdsForFiltering(categoryId);

        // Tạo specification để lọc sản phẩm
        Specification<Product> spec = createFilterSpecificationForCategories(categoryIds, null, null, filters);

        Page<Product> productPage = productRepo.findAll(spec, pageable);

        // Chuyển đổi thành DTO và trả về danh sách
        return productPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductFilterDTO> getProductsByBrand(
            String brandId,
            Map<String, String> filters,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        // Tạo specification để lọc sản phẩm
        Specification<Product> spec = createFilterSpecification(null, brandId, null, filters);

        Page<Product> productPage = productRepo.findAll(spec, pageable);

        // Chuyển đổi thành DTO và trả về danh sách
        return productPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy các thuộc tính lọc theo danh mục
    public Map<String, Object> getFiltersByCategory(String categoryId) {
        Map<String, Object> result = new HashMap<>();

        // Lấy các thuộc tính có thể lọc cho danh mục này (visible = true)
        List<Attribute> attributes = categoryAttributeRepo.findVisibleAttributesByCategoryId(categoryId);

        List<Map<String, Object>> filterList = new ArrayList<>();

        for (Attribute attribute : attributes) {
            Map<String, Object> filterInfo = new HashMap<>();
            filterInfo.put("id", attribute.getId());
            filterInfo.put("name", attribute.getName());

            // Lấy các giá trị thuộc tính
            List<AttributeValue> values = attributeValueRepo.findByAttribute_Id(attribute.getId());
            List<Map<String, Object>> valueList = new ArrayList<>();

            for (AttributeValue value : values) {
                Map<String, Object> valueInfo = new HashMap<>();
                valueInfo.put("id", value.getId());
                valueInfo.put("value", value.getValue());
                valueList.add(valueInfo);
            }

            filterInfo.put("values", valueList);
            filterList.add(filterInfo);
        }

        result.put("filters", filterList);
        return result;
    }

    // Lấy danh sách tất cả ID danh mục (danh mục hiện tại và các danh mục con)
    private List<String> getCategoryIdsForFiltering(String categoryId) {
        List<String> categoryIds = new ArrayList<>();
        categoryIds.add(categoryId);

        // Tìm các danh mục con có parentId = categoryId
        List<Category> childCategories = categoryRepo.findByParentId(categoryId);
        if (childCategories != null && !childCategories.isEmpty()) {
            for (Category child : childCategories) {
                categoryIds.add(child.getId());
            }
        }

        return categoryIds;
    }

    // Helper method để tạo specification cho việc lọc với nhiều danh mục
    private Specification<Product> createFilterSpecificationForCategories(
            List<String> categoryIds,
            String brandId,
            String productLineId,
            Map<String, String> filters) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Lọc theo danh mục (sử dụng IN để lọc theo nhiều danh mục)
            if (categoryIds != null && !categoryIds.isEmpty()) {
                predicates.add(root.get("category").get("id").in(categoryIds));
            }

            // Lọc theo thương hiệu
            if (brandId != null) {
                predicates.add(criteriaBuilder.equal(root.get("brand").get("id"), brandId));
            }

            // Lọc theo dòng sản phẩm
            if (productLineId != null) {
                predicates.add(criteriaBuilder.equal(root.get("product_line").get("id"), productLineId));
            }

            // Xử lý các bộ lọc
            if (filters != null && !filters.isEmpty()) {
                for (Map.Entry<String, String> entry : filters.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    // Kiểm tra nếu key có dạng "attr_{id}"
                    if (key.startsWith("attr_")) {
                        String attributeValueId = value;

                        // Join với ProductAttributeValue để lọc
                        Join<Product, ProductAttributeValue> pavJoin = root.join("productAttributeValues", JoinType.LEFT);

                        predicates.add(criteriaBuilder.equal(pavJoin.get("attributeValue").get("id"), attributeValueId));
                    }
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Giữ lại phương thức cũ để tương thích ngược
    private Specification<Product> createFilterSpecification(
            String categoryId,
            String brandId,
            String productLineId,
            Map<String, String> filters) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Lọc theo danh mục
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            // Lọc theo thương hiệu
            if (brandId != null) {
                predicates.add(criteriaBuilder.equal(root.get("brand").get("id"), brandId));
            }

            // Lọc theo dòng sản phẩm
            if (productLineId != null) {
                predicates.add(criteriaBuilder.equal(root.get("product_line").get("id"), productLineId));
            }

            // Xử lý các bộ lọc
            if (filters != null && !filters.isEmpty()) {
                for (Map.Entry<String, String> entry : filters.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    // Kiểm tra nếu key có dạng "attr_{id}"
                    if (key.startsWith("attr_")) {
                        String attributeValueId = value;

                        // Join với ProductAttributeValue để lọc
                        Join<Product, ProductAttributeValue> pavJoin = root.join("productAttributeValues", JoinType.LEFT);

                        predicates.add(criteriaBuilder.equal(pavJoin.get("attributeValue").get("id"), attributeValueId));
                    }
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Chuyển đổi entity sang DTO
    private ProductFilterDTO convertToDTO(Product product) {
        ProductFilterDTO dto = new ProductFilterDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(String.valueOf(product.getPrice()));
        dto.setQuantity(String.valueOf(product.getQuantity()));

        if (product.getImages() != null && !product.getImages().isEmpty()) {
            dto.setAvatar(product.getImages().get(0));
        }

        return dto;
    }
}