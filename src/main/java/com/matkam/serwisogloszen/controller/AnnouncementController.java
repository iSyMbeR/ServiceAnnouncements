package com.matkam.serwisogloszen.controller;

import com.matkam.serwisogloszen.model.Announcement;
import com.matkam.serwisogloszen.model.Category;
import com.matkam.serwisogloszen.service.AnnouncementService;
import com.matkam.serwisogloszen.service.CategoryService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.Set;

import static com.matkam.serwisogloszen.service.UserDetailsServiceImplementation.LOGGED_USER;

@Route("announcements/:announcementId?")
public class AnnouncementController extends VerticalLayout implements BeforeEnterObserver {

    public Long id;
    private Set<Announcement> selected;
    private final AnnouncementService announcementService;
    private final CategoryService categoryService;


    AnnouncementController(AnnouncementService announcementService, CategoryService categoryService) {
        this.announcementService = announcementService;
        this.categoryService = categoryService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getRouteParameters().get("announcementId").isPresent())
            this.id = Long.parseLong(event.getRouteParameters().get("announcementId").get());
        if (this.id != null) getLayoutAnnouncement();
        else getLayoutAnnouncements();
    }

    private void getLayoutAnnouncements() {
        this.removeAll();
        Label userName = new Label("Witaj "+LOGGED_USER.getUsername());
        add(userName);
        List<Announcement> announcements = announcementService.findAllAnnouncements();

        Grid<Announcement> grid = new Grid<>();
        grid.setItems(announcements);
        grid.addColumn(Announcement::getId).setHeader("ID");
        grid.addColumn(Announcement::getContent).setHeader("Name");
        grid.addColumn(Announcement -> Announcement.getCategory().getName()).setHeader("Category");
        grid.addItemClickListener(event ->
                UI.getCurrent().getPage().setLocation("http://localhost:8081/announcements/" + event.getItem().getId()));
        add(grid);
        TextArea content = new TextArea("Content");

        Select<String> select = new Select<>();
        select.setLabel("Category");
        List<Category> categories = categoryService.findAllCategories();
        select.setItems(categories.stream().map(Category::getName));


        VerticalLayout vl = new VerticalLayout(
                new H2("Add announcement"),
                content,
                select,
                new Button("Send", event -> addAnnouncement(
                        content.getValue(),
                        select.getValue()
                )));
        vl.setWidth(300,Unit.PIXELS);
        vl.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        vl.setHorizontalComponentAlignment(Alignment.CENTER);
        vl.setAlignSelf(Alignment.CENTER);
        add(vl);




    }

    public void getLayoutAnnouncement() {
        Label label = new Label(announcementService.findAnnouncementById(this.id).toString());
        add(label);
    }

    private void addAnnouncement(String content, String category) {
        if (content.trim().isEmpty()) {
            Notification.show("Enter a announcement message");
        } else if (category.isEmpty()) {
            Notification.show("Enter a category");
        } else {
            Category c = categoryService.findByName(category);
            announcementService.saveAnnouncement(new Announcement(content, c, LOGGED_USER));
            Notification.show("Added announcement!");
            getLayoutAnnouncements();

        }
    }
}
