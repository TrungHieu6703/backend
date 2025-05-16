// CategoryAttributeService.java
package com.example.backend.service;

import com.example.backend.dto.request.CategoryAttributeDTO;
import com.example.backend.dto.response.CategoryAttributeRes;
import com.example.backend.entity.Attribute;
import com.example.backend.entity.Category;
import com.example.backend.entity.CategoryAttribute;
import com.example.backend.repository.AttributeRepo;
import com.example.backend.repository.CategoryAttributeRepo;
import com.example.backend.repository.CategoryRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryAttributeService {

    @Autowired
    private CategoryAttributeRepo categoryAttributeRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private AttributeRepo attributeRepo;

    // Lấy tất cả mối quan hệ giữa danh mục và thuộc tính
    public List<CategoryAttributeDTO> getAllCategoryAttributes() {
        List<CategoryAttribute> entities = categoryAttributeRepo.findAll();
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy các thuộc tính của một danh mục cụ thể
    public List<CategoryAttributeDTO> getCategoryAttributesByCategoryId(String categoryId) {
        List<CategoryAttribute> entities = categoryAttributeRepo.findByCategoryId(categoryId);
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lưu hoặc cập nhật một mối quan hệ
    @Transactional
    public CategoryAttributeDTO saveCategoryAttribute(CategoryAttributeDTO dto) {
        CategoryAttribute entity = convertToEntity(dto);
        CategoryAttribute savedEntity = categoryAttributeRepo.save(entity);
        return convertToDTO(savedEntity);
    }

    // Lưu nhiều mối quan hệ cùng lúc
    @Transactional
    public List<CategoryAttributeDTO> saveBatchCategoryAttributes(List<CategoryAttributeDTO> dtos) {
        // Xóa tất cả các mối quan hệ hiện có (hoặc có thể xóa chọn lọc)
        // categoryAttributeRepo.deleteAll(); // Không nên xóa tất cả, thay vào đó xử lý từng trường hợp

        List<CategoryAttributeDTO> result = new ArrayList<>();

        for (CategoryAttributeDTO dto : dtos) {
            Optional<CategoryAttribute> existingOpt = categoryAttributeRepo.findByCategoryIdAndAttributeId(
                    dto.getCategoryId(), dto.getAttributeId());

            CategoryAttribute entity;
            if (existingOpt.isPresent()) {
                // Cập nhật nếu đã tồn tại
                entity = existingOpt.get();
                entity.setVisible(dto.isVisible());
                entity.setDisplay(dto.isDisplay());
                entity.setFilter(dto.isFilter());
            } else {
                // Tạo mới nếu chưa tồn tại
                entity = convertToEntity(dto);
            }

            CategoryAttribute savedEntity = categoryAttributeRepo.save(entity);
            result.add(convertToDTO(savedEntity));
        }

        return result;
    }

    // Xóa một mối quan hệ
    @Transactional
    public void deleteCategoryAttribute(String categoryId, String attributeId) {
        categoryAttributeRepo.deleteByCategoryIdAndAttributeId(categoryId, attributeId);
    }

    // Chuyển đổi từ Entity sang DTO
    private CategoryAttributeDTO convertToDTO(CategoryAttribute entity) {
        CategoryAttributeDTO dto = new CategoryAttributeDTO();
        dto.setCategoryId(entity.getCategory().getId());
        dto.setAttributeId(entity.getAttribute().getId());
        dto.setVisible(entity.isVisible());
        dto.setDisplay(entity.isDisplay());
        dto.setFilter(entity.isFilter());
        return dto;
    }

    // Chuyển đổi từ DTO sang Entity
    private CategoryAttribute convertToEntity(CategoryAttributeDTO dto) {
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + dto.getCategoryId()));

        Attribute attribute = attributeRepo.findById(dto.getAttributeId())
                .orElseThrow(() -> new RuntimeException("Attribute not found with ID: " + dto.getAttributeId()));

        CategoryAttribute entity = new CategoryAttribute();
        entity.setCategory(category);
        entity.setAttribute(attribute);
        entity.setVisible(dto.isVisible());
        entity.setDisplay(dto.isDisplay());
        entity.setFilter(dto.isFilter());

        return entity;
    }

    // Chuyển từ Entity sang Response DTO
    private CategoryAttributeRes convertToResponseDTO(CategoryAttribute entity) {
        CategoryAttributeRes res = new CategoryAttributeRes();
        res.setId(entity.getId());
        res.setCategoryId(entity.getCategory().getId());
        res.setAttributeId(entity.getAttribute().getId());
        res.setVisible(entity.isVisible());
        res.setDisplay(entity.isDisplay());
        res.setFilter(entity.isFilter());
        return res;
    }
}