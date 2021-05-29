//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.matkam.serwisogloszen.controller;

import com.matkam.serwisogloszen.model.Category;
import com.matkam.serwisogloszen.service.CategoryService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("category")
public class CategoryController extends VerticalLayout {
    private final CategoryService categoryService;

    CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
        this.getLayoutAddCategory();
    }

    private void getLayoutAddCategory() {
        TextField name = new TextField("Name");
        this.add(new VerticalLayout(new H2("Add category"),
                        name,
                        new Button("Send", (event) -> {
                            this.addCategory(name.getValue());
                        })
                )
        );
    }

    private void addCategory(String name) {
        if (name.trim().isEmpty()) {
            Notification.show("Enter a announcement message");
        } else {
            this.categoryService.saveCategory(new Category(name));
            Notification.show("Added category!");
        }

    }
}
