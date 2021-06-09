package com.matkam.ServiceAnnouncements.service;

import com.matkam.ServiceAnnouncements.exceptions.AnnouncementNotFountException;
import com.matkam.ServiceAnnouncements.model.announcement.Announcement;
import com.matkam.ServiceAnnouncements.model.announcement.AnnouncementStatus;
import com.matkam.ServiceAnnouncements.repository.AnnouncementRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepo announcementRepo;
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementService.class);

    public List<Announcement> findAllAnnouncements() {
        LOGGER.info("Getting all announcements");
        return announcementRepo.findAll();
    }

    public Announcement findAnnouncementById(Long id) {
        LOGGER.info("Getting all announcements by ID " + id);
        return announcementRepo.findById(id)
                .orElseThrow(() -> new AnnouncementNotFountException("Announcement with id " + id + " was not found"));
    }

    public void saveAnnouncement(Announcement announcement) {
        announcementRepo.save(announcement);
        LOGGER.info("Saved announcement with id" + announcement.getId());
    }

    public void deleteAnnouncement(Announcement announcement) {
        announcementRepo.delete(announcement);
        LOGGER.info("Deleted announcement with id " + announcement.getId());
    }

    public void deleteAnnouncementById(Long id) {
        announcementRepo.deleteById(id);
        LOGGER.info("Deleted announcement with id" + id);
    }

    public List<Announcement> findByStatus(AnnouncementStatus status) {
        LOGGER.info("Getting announcement by status " + status.name());
        return announcementRepo.findByStatusEquals(status);
    }

    ;
}
