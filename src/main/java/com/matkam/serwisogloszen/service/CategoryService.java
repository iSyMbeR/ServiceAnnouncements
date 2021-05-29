package com.matkam.serwisogloszen.service;

import com.matkam.serwisogloszen.exceptions.CategoryNotFountException;
import com.matkam.serwisogloszen.model.Category;
import com.matkam.serwisogloszen.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepo categoryRepository;

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findCategoriesById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFountException("Category with id " + id + " was not found"));
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category findByName(String name){
        return categoryRepository.findByName(name);
    }
}
