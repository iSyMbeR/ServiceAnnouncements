package com.matkam.serwisogloszen.controller;

import com.matkam.serwisogloszen.model.Category;
import com.matkam.serwisogloszen.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.atmosphere.config.service.Get;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/generate/category")
    public String createRandomCategories() {
        List<String> categoryNames = Arrays.asList("All", "Arts & Crafts", "Automotive", "Baby", "Beauty & Personal Care", "Books", "Computers", "Digital Music", "Electronics", "Kindle Store",
                "Prime Video", "Video Games", "Toys & Games", "Tools & Home Improvement", "Sports & Outdoors", "Software", "Pet Supplies", "Music, CDs & Vinyl", "Movies & TV", "Luggage", "Industrial & Scientific",
                "Home & Kitchen", "Health & Household", "Deals", "Boys' Fashion", "Girls' Fashion", "Men's Fashion", "Women's Fashion");

        categoryNames.forEach(s -> {
            categoryService.saveCategory(Category.builder()
                    .name(s)
                    .build());
            System.out.println("Dodano " +s);
        });
        return "dodano";
    }
}
