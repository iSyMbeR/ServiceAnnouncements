package com.matkam.ServiceAnnouncements.schedule;

import com.matkam.ServiceAnnouncements.model.announcement.Announcement;
import com.matkam.ServiceAnnouncements.model.announcement.AnnouncementStatus;
import com.matkam.ServiceAnnouncements.model.user.User;
import com.matkam.ServiceAnnouncements.service.AnnouncementService;
import com.matkam.ServiceAnnouncements.service.SendMailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final AnnouncementService announcementService;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final SendMailService sendMailService;

    //https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling-cron-expression
    @Scheduled(cron = "*/10 * * * * *")
    public void deleteBlockedAnnouncements() {
        List<Announcement> announcementList = announcementService.findAllAnnouncements();
        log.info("Searching for advertisements to delete");
        List<Announcement> listToDelete = announcementList.stream().filter(announcement ->
                announcement.getStatus().equals(AnnouncementStatus.blocked)).collect(Collectors.toList());
        for (Announcement a : listToDelete) {
            announcementService.deleteAnnouncementById(a.getId());
            log.info("Announcement with id {} created by user {} was removed due to a blockage ", a.getId(), a.getUser().getUsername());
            sendInfoWhenBlockedAndDeletedAnnouncement(a.getUser());
        }
    }

    @Scheduled(cron = "*/20 * * * * *")
    public void outdatedAnnouncements() {
        log.info("Searching for advertisements to outdated");
        List<Announcement> announcementList = announcementService.findAllAnnouncements();
        List<Announcement> listOut = announcementList.stream().filter(announcement ->
                announcement.getStatus().equals(AnnouncementStatus.active)).collect(Collectors.toList());

        if (!listOut.isEmpty()) {
            for (Announcement a : listOut) {
                System.out.println("WESZLO TU przed ifem" );
                System.out.println();
                if (a.getFinishDate() == null) {
                    LocalDateTime actualDate = LocalDateTime.now();
                    LocalDateTime finishDate = actualDate.plusSeconds(30);
                    System.out.println("WESZLO TU " + finishDate);
                    a.setFinishDate(finishDate);
                    announcementService.saveAnnouncement(a);
                }
            }

            List<Announcement> listToChangeStatus = listOut.stream().filter(announcement -> LocalDateTime.now().isAfter(announcement.getFinishDate()))
                    .collect(Collectors.toList());

            for (Announcement a : listToChangeStatus) {
                System.out.println("Data wygasniecia: " + a.getFinishDate() + " I data terazniejsza" + LocalDateTime.now());
                a.setStatus(AnnouncementStatus.outdated);
                a.setFinishDate(null);
                announcementService.saveAnnouncement(a);
                log.info("Announcement with id {} created by user {} was outdated due to a time-out ", a.getId(), a.getUser().getUsername());
                sendOutdatedInfo(a.getUser());
            }
        }
    }

    public LocalDateTime convertToLocalDateTime(Date dateToConvert) {
//        dateFormat.format(dateToConvert);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return dateToConvert.toInstant().atZone(defaultZoneId).toLocalDateTime();
    }

    private void sendInfoWhenBlockedAndDeletedAnnouncement(User user) {
        log.info("Sending sendInfoWhenBlockedAndDeletedAnnouncement to user " + user.getUsername());
        String message = "Your Announcement has been blocked and deleted.";
        sendMailService.sendMail(user.getEmail(), message, "Announcement!");
        log.info("Message sent");
    }

    private void sendOutdatedInfo(User user) {
        log.info("Sending sendOutdatedInfo to user " + user.getUsername());
        String message = "Your Announcement has been outdated";
        sendMailService.sendMail(user.getEmail(), message, "Announcement!");
        log.info("Message sent");
    }
}
