package com.matkam.ServiceAnnouncements.controller;

import com.matkam.ServiceAnnouncements.model.announcement.Announcement;
import com.matkam.ServiceAnnouncements.model.announcement.AnnouncementStatus;
import com.matkam.ServiceAnnouncements.model.user.User;
import com.matkam.ServiceAnnouncements.service.AnnouncementService;
import com.matkam.ServiceAnnouncements.service.CategoryService;
import com.matkam.ServiceAnnouncements.service.SendMailService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.matkam.ServiceAnnouncements.service.UserDetailsServiceImplementation.LOGGED_USER;


@Route("announcements-admin")
public class AnnouncementAdminPanel extends VerticalLayout {
    private static final String HOST = "localhost";
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementAdminPanel.class);
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
        LOGGER.info(LOGGED_USER.getUsername() + " Connected to " + HOST + "/announcements-admin");
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
        LOGGER.info("Changing AnnouncementStatus by" + LOGGED_USER.getUsername());
        if (this.selected.size() == 0) return;
        if (status.equals(AnnouncementStatus.active)) {
            for (Announcement a : this.selected) {
                LOGGER.info("Changing AnnouncementStatus with id " + a.getId() + " created by "
                        + a.getUser().getUsername() + "from " + a.getStatus() + " to " + status);
                a.setStatus(status);
                announcementService.saveAnnouncement(a);
                sendActiveAnnouncementInformation(a.getUser());
                LOGGER.info("AnnouncementStatus changed");
            }
        } else {
            for (Announcement a : this.selected) {
                LOGGER.info("Changing AnnouncementStatus with id " + a.getId() + " created by "
                        + a.getUser().getUsername() + "from " + a.getStatus() + " to " + status);
                a.setStatus(status);
                a.setStatus(status);
                announcementService.saveAnnouncement(a);
                sendBlockAnnouncementInformation(a.getUser());
                LOGGER.info("AnnouncementStatus changed");
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
        LOGGER.info("Removing selected announcements by " + LOGGED_USER.getUsername());
        if (this.selected.size() == 0) return;
        for (Announcement a : this.selected) {
            announcementService.deleteAnnouncement(a);
            sendDeleteAnnouncementInformation(a.getUser());
            LOGGER.info("Announcement with id '" + a.getId() + "' created by user '"
                    + a.getUser().getUsername() + "' has been deleted");
        }
        this.selected = new HashSet<>();
        getLayoutAnnouncements();
    }

    private void sendActiveAnnouncementInformation(User user) {
        LOGGER.info("Sending ActiveAnnouncementInformation to user " + user.getUsername());
        String message = "Your advertisement has been added.";
        sendMailService.sendMail(user.getEmail(), message, "Announcement!");
        LOGGER.info("Message sent");
    }

    private void sendBlockAnnouncementInformation(User user) {
        LOGGER.info("Sending BlockAnnouncementInformation to user " + user.getUsername());
        String message = "Your advertisement has been blocked.";
        sendMailService.sendMail(user.getEmail(), message, "Announcement!");
        LOGGER.info("Message sent");
    }

    private void sendDeleteAnnouncementInformation(User user) {
        LOGGER.info("Sending DeleteAnnouncementInformation to user " + user.getUsername());
        String message = "Your advertisement has been deleted.";
        sendMailService.sendMail(user.getEmail(), message, "Announcement!");
        LOGGER.info("Message sent");
    }
}
