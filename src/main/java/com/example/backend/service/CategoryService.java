package com.example.backend.service;

import com.example.backend.dto.request.CategoryDTO;
import com.example.backend.dto.response.CategoryRes;
import com.example.backend.entity.Brand;
import com.example.backend.entity.Category;
import com.example.backend.repository.CategoryRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    public CategoryRes createCategory(CategoryDTO CategoryDTO) {
        Category Category = new Category();
        Category.setName(CategoryDTO.getName());
        Category.setParentId(CategoryDTO.getParentId());
        Category result = categoryRepo.save(Category);

        return new CategoryRes(result.getId(), result.getName());
    }

    public CategoryRes updateCategory(String id ,CategoryDTO CategoryDTO) {
        Category Category = categoryRepo.findById(id)
                .orElseThrow(()-> new RuntimeException(("Category not found")));

        Category.setName(CategoryDTO.getName());
        Category.setParentId(CategoryDTO.getParentId());
        Category updatedCategory = categoryRepo.save(Category);

        return new CategoryRes(updatedCategory.getId(), updatedCategory.getName());
    }

    public void deleteCategory(String categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Brand not found with id: " + categoryId));

        // Kiểm tra xem brand có liên kết với sản phẩm nào không
        boolean hasProducts = categoryRepo.existsProductsByCategoryId(categoryId);

        if (hasProducts) {
            // Cập nhật trường is_deleted nếu có liên kết
            category.set_deleted(true);
            categoryRepo.save(category);
        } else {
            // Xóa hoàn toàn nếu không có liên kết
            categoryRepo.delete(category);
        }
    }

    public CategoryRes getCategoryById(String id) {
        Category Category = categoryRepo.findById(id)
                .orElseThrow(()-> new RuntimeException(("Category not found")));
        return new CategoryRes(
                Category.getId(),
                Category.getName()
        );
    }

    public List<CategoryRes> getAllCategorys() {
        List<Category> Categorys = categoryRepo.findAllActiveCategories();

        return Categorys.stream()
                .map(Category -> new CategoryRes(
                        Category.getId(),
                        Category.getName()
                )).collect(Collectors.toList());
    }
}
