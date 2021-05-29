package com.matkam.serwisogloszen.controller;

import com.matkam.serwisogloszen.model.Announcement;
import com.matkam.serwisogloszen.service.AnnouncementService;
import com.matkam.serwisogloszen.service.CategoryService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Route("announcements-admin/:announcementId?")
public class AnnouncementAdminPanel extends VerticalLayout implements BeforeEnterObserver {

    public Long id;
    private Set<Announcement> selected;
    private final AnnouncementService announcementService;
    private final CategoryService categoryService;

    AnnouncementAdminPanel(AnnouncementService announcementService, CategoryService categoryService) {
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
        List<Announcement> announcements = announcementService.findAllAnnouncements();

        Grid<Announcement> grid = new Grid<>();
        grid.setItems(announcements);
        grid.addColumn(Announcement::getId).setHeader("ID");
        grid.addColumn(Announcement::getContent).setHeader("Name");
        grid.addColumn(Announcement -> Announcement.getCategory().getName()).setHeader("Category");
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addSelectionListener(event -> {
            this.selected = event.getAllSelectedItems();
        });
        grid.addItemClickListener(event ->
                UI.getCurrent().getPage().setLocation("http://localhost:8081/announcements-admin/" + event.getItem().getId()));
        add(grid);

        add(new Button("Usuń", event -> removeSelectedAnnouncements()));

    }

    public void getLayoutAnnouncement() {
        Label label = new Label(announcementService.findAnnouncementById(this.id).toString());
        add(label);
    }

    private void removeSelectedAnnouncements() {
        if (this.selected.size() == 0) return;
        for (Announcement a : this.selected) {
            announcementService.deleteAnnouncement(a);
        }
        this.selected = new HashSet<Announcement>();
        getLayoutAnnouncements();
    }

}