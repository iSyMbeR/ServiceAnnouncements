package com.matkam.serwisogloszen.service;

import com.matkam.serwisogloszen.model.Category;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
//czyszczenie bazy po kazdym
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor
class CategoryServiceTest {

    private List<String> categoryNames;
    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    void fillDataBase() {
        categoryNames = Arrays.asList("All", "Arts & Crafts", "I dont know");
        categoryNames.forEach(s -> {
            categoryService.saveCategory(Category.builder()
                    .name(s)
                    .build());
        });
        assert categoryService.findAllCategories() != null;
    }

    @Test
    void shouldReturnAllCategories() {
        Assertions.assertEquals(categoryNames.size(), categoryService.findAllCategories().size());
    }

    @Test
    void shouldReturnCategoryById() {
        Category category = categoryService.findCategoriesById(1L);
        assert category.getName().equals(categoryNames.get(0));
    }

    @Test
    void shouldSaveCategoryToDatabase() {
        Category category = Category.builder()
                .name("Biedronka")
                .build();
        categoryService.saveCategory(category);
        Assertions.assertEquals(categoryNames.size() + 1, categoryService.findAllCategories().size());
    }

    @Test
    void shouldDeleteCategoryFromDb() {
        categoryService.deleteCategory(categoryService.findByName(categoryNames.get(0)));
        Assertions.assertEquals(categoryNames.size() - 1, categoryService.findAllCategories().size());
    }

    @Test
    void shouldDeleteCategoryById() {
        categoryService.deleteCategoryById(categoryService.findByName(categoryNames.get(0)).getId());
        Assertions.assertEquals(categoryNames.size() - 1, categoryService.findAllCategories().size());
    }

    @Test
    void shouldFindCategoryByName() {
        Category category = categoryService.findByName(categoryNames.get(1));
        Assertions.assertEquals(categoryNames.get(1), category.getName());
    }
}
