package com.matkam.serwisogloszen.controller;

import com.matkam.serwisogloszen.model.announcement.Announcement;
import com.matkam.serwisogloszen.model.announcement.AnnouncementStatus;
import com.matkam.serwisogloszen.model.user.User;
import com.matkam.serwisogloszen.service.AnnouncementService;
import com.matkam.serwisogloszen.service.CategoryService;
import com.matkam.serwisogloszen.service.SendMailService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Route("announcements-admin")
public class AnnouncementAdminPanel extends VerticalLayout {
    public Long id;
    private Set<Announcement> selected;
    private final AnnouncementService announcementService;
    private final CategoryService categoryService;
    private final SendMailService sendMailService;

    AnnouncementAdminPanel(AnnouncementService announcementService, CategoryService categoryService, SendMailService sendMailService) {
        this.announcementService = announcementService;
        this.categoryService = categoryService;
        this.sendMailService = sendMailService;
        this.getLayoutAnnouncements();
    }


    private void getLayoutAnnouncements() {
        this.removeAll();
        List<Announcement> announcements = announcementService.findAllAnnouncements();

        Grid<Announcement> grid = new Grid<>();
        grid.setItems(announcements);
        grid.addColumn(Announcement::getId).setHeader("ID");
        grid.addColumn(Announcement::getContent).setHeader("Name");
        grid.addColumn(Announcement -> Announcement.getCategory().getName()).setHeader("Category");
        grid.addColumn(Announcement -> Announcement.getStatus().toString()).setHeader("Status");
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addSelectionListener(event -> {
            this.selected = event.getAllSelectedItems();
        });
        grid.addItemClickListener(event ->
                UI.getCurrent().getPage().setLocation("http://localhost:8081/announcements/" + event.getItem().getId()));
        add(grid);

        HorizontalLayout vl = new HorizontalLayout(
                new Button("Usuń", event -> removeSelectedAnnouncements()),
                new Button("Activate", event -> changeStatus(AnnouncementStatus.active)),
                new Button("Block", event -> changeStatus(AnnouncementStatus.blocked))
        );
        add(vl);

    }

    private void changeStatus(AnnouncementStatus status) {
        if (this.selected.size() == 0) return;
        if (status.equals(AnnouncementStatus.active)) {
            for (Announcement a : this.selected) {
                a.setStatus(status);
                announcementService.saveAnnouncement(a);
                sendActiveAnnouncementInformation(a.getUser());
            }
        } else {
            for (Announcement a : this.selected) {
                a.setStatus(status);
                announcementService.saveAnnouncement(a);
                sendBlockAnnouncementInformation(a.getUser());
            }
        }
        this.selected = new HashSet<>();
        getLayoutAnnouncements();

    }

//    public void getLayoutAnnouncement() {
//        Label label = new Label(announcementService.findAnnouncementById(this.id).toString());
//        add(label);
//    }

    private void removeSelectedAnnouncements() {
        if (this.selected.size() == 0) return;
        for (Announcement a : this.selected) {
            announcementService.deleteAnnouncement(a);
            sendDeleteAnnouncementInformation(a.getUser());
        }
        this.selected = new HashSet<>();
        getLayoutAnnouncements();
    }

    private void sendActiveAnnouncementInformation(User user) {
        String message = "Your advertisement has been added.";
        sendMailService.sendMail(user.getEmail(), message, "Announcement!");
    }

    private void sendBlockAnnouncementInformation(User user) {
        String message = "Your advertisement has been blocked.";
        sendMailService.sendMail(user.getEmail(), message, "Announcement!");
    }

    private void sendDeleteAnnouncementInformation(User user) {
        String message = "Your advertisement has been deleted.";
        sendMailService.sendMail(user.getEmail(), message, "Announcement!");
    }
}
