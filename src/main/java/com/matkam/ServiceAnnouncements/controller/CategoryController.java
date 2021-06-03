//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.matkam.ServiceAnnouncements.controller;

import com.matkam.ServiceAnnouncements.model.Category;
import com.matkam.ServiceAnnouncements.service.CategoryService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.matkam.ServiceAnnouncements.service.UserDetailsServiceImplementation.LOGGED_USER;

@Route("category/:categoryId?")
public class CategoryController extends VerticalLayout implements BeforeEnterObserver {
    private static final String HOST = "localhost";
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;
    private Long id;
    private Set<Category> selected;

    CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
        this.getLayoutCategories();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getRouteParameters().get("categoryId").isPresent())
            this.id = Long.parseLong(event.getRouteParameters().get("categoryId").get());
        if (this.id == null) this.getLayoutCategories();
        else getLayoutCategory();
    }

    public void getLayoutCategory() {
        LOGGER.info(LOGGED_USER.getUsername() + " Connected to " + HOST + "/category");
        this.removeAll();
        Category category = categoryService.findCategoriesById(this.id);

        TextArea name = new TextArea("Name");
        name.setValue(category.getName());

        VerticalLayout vl = new VerticalLayout(
                new H2("Edit announcement"),
                name,
                new Button("Save", event -> saveCategory(
                        name.getValue(),
                        category
                )));

        add(vl);
    }

    private void saveCategory(String name, Category category) {
        LOGGER.info("Saving category");
        if (name.trim().isEmpty()) {
            Notification.show("Enter a announcement message");
        } else {
            category.setName(name);
            categoryService.saveCategory(category);
            Notification.show("Saved category!");
            LOGGER.info("Category saved");
            UI.getCurrent().getPage().setLocation("http://localhost:8081/category/");
        }

    }

    private void getLayoutCategories() {
        this.removeAll();
        List<Category> categories = categoryService.findAllCategories();
        Grid<Category> grid = new Grid<>();
        grid.setItems(categories);
        grid.addColumn(Category::getId).setHeader("ID");
        grid.addColumn(Category::getName).setHeader("Name");
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addSelectionListener(event -> {
            this.selected = event.getAllSelectedItems();
        });
        grid.addItemClickListener(event ->
                UI.getCurrent().getPage().setLocation("http://localhost:8081/category/" + event.getItem().getId()));
        add(grid);
        add(new Button("Usuń", event -> removeSelectedCategories()));

        TextField name = new TextField("Name");
        this.add(new VerticalLayout(new H2("Add category"),
                        name,
                        new Button("Send", (event) -> {
                            this.addCategory(name.getValue());
                        })
                )
        );
    }

    private void removeSelectedCategories() {
        LOGGER.info("Removing selected category");
        if (this.selected.size() == 0) return;
        for (Category c : this.selected) {
            categoryService.deleteCategory(c);
            LOGGER.info("Category " + c.getName() + " deleted");
        }
        this.selected = new HashSet<>();
        getLayoutCategories();
    }

    private void addCategory(String name) {
        LOGGER.info("Adding new category'" + name + "'");
        if (name.trim().isEmpty()) {
            Notification.show("Enter a announcement message");
        } else {
            this.categoryService.saveCategory(new Category(name));
            Notification.show("Added category!");
            LOGGER.info(name + " category added");
        }
        getLayoutCategories();
    }
}
