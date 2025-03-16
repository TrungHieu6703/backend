package com.example.backend.service;

import com.example.backend.dto.request.CategoryDTO;
import com.example.backend.dto.response.CategoryRes;
import com.example.backend.entity.Category;
import com.example.backend.repository.CategoryRepo;
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

        Category result = categoryRepo.save(Category);

        return new CategoryRes(result.getId(), result.getName());
    }

    public CategoryRes updateCategory(String id ,CategoryDTO CategoryDTO) {
        Category Category = categoryRepo.findById(id)
                .orElseThrow(()-> new RuntimeException(("Category not found")));

        Category.setName(CategoryDTO.getName());

        Category updatedCategory = categoryRepo.save(Category);

        return new CategoryRes(updatedCategory.getId(), updatedCategory.getName());
    }

    public void deleteCategory(String id) {
        Category Category = categoryRepo.findById(id).orElseThrow(()-> new RuntimeException(("Category not found")));
        categoryRepo.delete(Category);
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
        List<Category> Categorys = categoryRepo.findAll();

        return Categorys.stream()
                .map(Category -> new CategoryRes(
                        Category.getId(),
                        Category.getName()
                )).collect(Collectors.toList());
    }
}
