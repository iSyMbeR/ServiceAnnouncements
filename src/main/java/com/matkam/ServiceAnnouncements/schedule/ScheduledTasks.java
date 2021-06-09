package com.matkam.ServiceAnnouncements.schedule;

import com.matkam.ServiceAnnouncements.model.announcement.Announcement;
import com.matkam.ServiceAnnouncements.model.announcement.AnnouncementStatus;
import com.matkam.ServiceAnnouncements.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final AnnouncementService announcementService;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

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
        }
    }

//    @Scheduled(cron = "*/20 * * * * *")
//    public void outDatedAnnouncements() {
//        List<Announcement> announcementList = announcementService.findAllAnnouncements();
//        List<Announcement> listOut = announcementList.stream().filter(announcement ->
//                announcement.getStatus().equals(AnnouncementStatus.active)).collect(Collectors.toList());
//
//        if(!listOut.isEmpty()){
//            for(Announcement a: listOut){
//                if(a.getFinishDate() == null){
////
////                    LocalDate actualDate = LocalDate.of();
////                    LocalDate finishDate = actualDate
////                    a.setFinishDate();
////                    //sprwadzanie czy minelo iles tam czasu i usuwanie
//                }
//            }
//        }
//    }
}
