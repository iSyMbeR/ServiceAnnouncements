package com.matkam.ServiceAnnouncements.controller;

import com.matkam.ServiceAnnouncements.model.Category;
import com.matkam.ServiceAnnouncements.model.announcement.Announcement;
import com.matkam.ServiceAnnouncements.model.announcement.AnnouncementStatus;
import com.matkam.ServiceAnnouncements.service.AnnouncementService;
import com.matkam.ServiceAnnouncements.service.CategoryService;
import com.matkam.ServiceAnnouncements.service.SendMailService;
import com.matkam.ServiceAnnouncements.service.UserAppService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

import static com.matkam.ServiceAnnouncements.service.UserDetailsServiceImplementation.LOGGED_USER;

@Slf4j
@Route("announcements/:announcementId?")
public class AnnouncementController extends VerticalLayout implements BeforeEnterObserver {
    private static final String HOST = "localhost";
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementController.class);
    public Long id;
    private Set<Announcement> selected;
    private final AnnouncementService announcementService;
    private final CategoryService categoryService;
    private final UserAppService userAppService;
    private final SendMailService sendMailService;

    AnnouncementController(AnnouncementService announcementService, CategoryService categoryService, UserAppService userAppService, SendMailService sendMailService) {
        this.announcementService = announcementService;
        this.categoryService = categoryService;
        this.userAppService = userAppService;
        this.sendMailService = sendMailService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getRouteParameters().get("announcementId").isPresent())
            this.id = Long.parseLong(event.getRouteParameters().get("announcementId").get());
        if (this.id != null) getLayoutAnnouncement();
        else getLayoutAnnouncements();
    }

    private void getLayoutAnnouncements() {
        LOGGER.info(LOGGED_USER.getUsername() + " Connected to " + HOST + "/announcements");
        this.removeAll();
        Label userName = new Label("Witaj " + LOGGED_USER.getUsername());
        add(userName);
        List<Announcement> announcements = announcementService.findByStatus(AnnouncementStatus.active);

        Grid<Announcement> grid = new Grid<>();
        grid.setItems(announcements);
        grid.addColumn(Announcement -> Announcement.getUser().getUsername()).setHeader("Added by");
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
        vl.setWidth(300, Unit.PIXELS);
        vl.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        vl.setHorizontalComponentAlignment(Alignment.CENTER);
        vl.setAlignSelf(Alignment.CENTER);
        add(vl);


    }

    public void getLayoutAnnouncement() {
        Announcement announcement = announcementService.findAnnouncementById(this.id);

        TextArea content = new TextArea("Content");
        content.setValue(announcement.getContent());

        Select<Category> categorySelect = new Select<>();
        categorySelect.setLabel("Category");
        categorySelect.setItems(categoryService.findAllCategories());
        categorySelect.setItemLabelGenerator(Category::getName);
        categorySelect.setValue(announcement.getCategory());

        VerticalLayout vl = new VerticalLayout(
                new H2("Edit announcement"),
                content,
                categorySelect,
                new Button("Save", event -> saveAnnouncement(
                        content.getValue(),
                        categorySelect.getValue(),
                        announcement
                )));
        add(vl);
    }

    private void addAnnouncement(String content, String category) {
        LOGGER.info(LOGGED_USER.getUsername() + " adding announcement");
        if (content.trim().isEmpty()) {
            Notification.show("Enter a announcement message");
        } else if (category.isEmpty()) {
            Notification.show("Enter a category");
        } else {
            Category c = categoryService.findByName(category);
            Announcement announcement = new Announcement(content, c, LOGGED_USER, AnnouncementStatus.review);
            LOGGED_USER.addAnnouncement(announcement);
            userAppService.saveUser(LOGGED_USER);
            Notification.show("Added announcement!");
            sendAddAnnouncementInformation();
            LOGGER.info("Announcement sent");
            getLayoutAnnouncements();
        }
    }

    private void saveAnnouncement(String content, Category category, Announcement announcement) {
        LOGGER.info("Saving AnnouncementInformation with id " + announcement.getId());
        if (content.trim().isEmpty()) {
            Notification.show("Enter a announcement message");
        } else if (category == null) {
            Notification.show("Enter a category");
        } else {
            announcement.setCategory(category);
            announcement.setContent(content);
            announcementService.saveAnnouncement(announcement);
            Notification.show("Saved announcement!");
            LOGGER.info("AnnouncementInformation saved");
            UI.getCurrent().getPage().setLocation("http://localhost:8081/announcements/");
        }
    }

    private void sendAddAnnouncementInformation() {
        LOGGER.info("Sending AddAnnouncementInformation to user " + LOGGED_USER.getUsername());
        String message = "Your advertisement has been sent to the administrator and is waiting for confirmation.";
        sendMailService.sendMail(LOGGED_USER.getEmail(), message, "Announcement!");
        LOGGER.info("AddAnnouncementInformation sent");
    }
}
