package com.matkam.serwisogloszen.controller;

import com.matkam.serwisogloszen.model.Announcement;
import com.matkam.serwisogloszen.service.AnnouncementService;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import java.util.List;


@Route("announcements/:announcementId?")
public class AnnouncementController extends VerticalLayout implements BeforeEnterObserver {

    public Long id;
    private final AnnouncementService announcementService;
    AnnouncementController(AnnouncementService announcementService){
        this.announcementService = announcementService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getRouteParameters().get("announcementId").isPresent())
            this.id = Long.parseLong(event.getRouteParameters().get("announcementId").get());
        if (this.id != null) getLayoutAnnouncement();
        else getLayoutAnnouncements();


    }

    private void getLayoutAnnouncements() {
        List<Announcement> announcements = announcementService.findAllAnnouncements();
        for(Announcement a : announcements){
            Label l = new Label(a.getId().toString());
            add(l);
        }

    }

    public void getLayoutAnnouncement() {
        Label label = new Label(announcementService.findAnnouncementById(this.id).toString());
        add(label);
    }
}
