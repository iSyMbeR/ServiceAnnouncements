package com.matkam.serwisogloszen.service;

import com.matkam.serwisogloszen.exceptions.CategoryNotFountException;
import com.matkam.serwisogloszen.model.Category;
import com.matkam.serwisogloszen.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepo categoryRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);

    public List<Category> findAllCategories() {
        LOGGER.info("Getting all categories");
        return categoryRepository.findAll();
    }

    public Category findCategoriesById(Long id) {
        LOGGER.info("Getting category by id " + id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFountException("Category with id " + id + " was not found"));
    }

    public Category saveCategory(Category category) {
        LOGGER.info("Saving category " + category.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
        LOGGER.info("Deleted category " + category.getName());
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
        LOGGER.info("Deleted category by ID " + id);
    }

    public Category findByName(String name) {
        LOGGER.info("Getting category by name " + name);
        return categoryRepository.findByName(name);
    }
}
