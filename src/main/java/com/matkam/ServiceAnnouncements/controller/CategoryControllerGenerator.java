package com.matkam.ServiceAnnouncements.controller;

import com.matkam.ServiceAnnouncements.model.Category;
import com.matkam.ServiceAnnouncements.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryControllerGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryControllerGenerator.class);

    private final CategoryService categoryService;

    @GetMapping("/generate/category")
    public String createRandomCategories() {
        LOGGER.info("Adding new categories ");
        List<String> categoryNames = Arrays.asList("All", "Arts & Crafts", "Automotive", "Baby", "Beauty & Personal Care", "Books", "Computers", "Digital Music", "Electronics", "Kindle Store",
                "Prime Video", "Video Games", "Toys & Games", "Tools & Home Improvement", "Sports & Outdoors", "Software", "Pet Supplies", "Music, CDs & Vinyl", "Movies & TV", "Luggage", "Industrial & Scientific",
                "Home & Kitchen", "Health & Household", "Deals", "Boys' Fashion", "Girls' Fashion", "Men's Fashion", "Women's Fashion");

        if(categoryService.findAllCategories().size() < categoryNames.size()){
            categoryNames.forEach(s -> {
                categoryService.saveCategory(Category.builder()
                        .name(s)
                        .build());
            });
            LOGGER.info("Added");
            return "Added";
        }
        LOGGER.warn("This was added earlier.");
        return "This was added earlier.";
    }
}
