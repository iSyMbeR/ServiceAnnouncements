package com.matkam.ServiceAnnouncements.schedule;

import com.matkam.ServiceAnnouncements.model.announcement.Announcement;
import com.matkam.ServiceAnnouncements.model.announcement.AnnouncementStatus;
import com.matkam.ServiceAnnouncements.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final AnnouncementService announcementService;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private List<Announcement> announcementList;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

//    @EventListener(ApplicationReadyEvent.class)
//    @Scheduled(cron = "*/19 * * * * *")
//    public void refreshList() {
//        if (announcementList == null) {
//            announcementList = new ArrayList<>();
//        }
//        announcementList = announcementService.findAllAnnouncements();
//    }

    //https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling-cron-expression
    @Scheduled(cron = "*/10 * * * * *")
    public void deleteBlockedAnnouncements() {
        announcementList = announcementService.findAllAnnouncements();
        log.info("Searching for advertisements to delete");
        List<Announcement> listToDelete = announcementList.stream().filter(announcement ->
                announcement.getStatus().equals(AnnouncementStatus.blocked)).collect(Collectors.toList());
        for (Announcement a : listToDelete) {
            announcementService.deleteAnnouncementById(a.getId());
            log.info("Announcement with id {} created by user {} was removed due to a blockage ", a.getId(), a.getUser().getUsername());
        }
    }
}
