package com.matkam.serwisogloszen.service;

import com.matkam.serwisogloszen.exceptions.AnnouncementNotFountException;
import com.matkam.serwisogloszen.model.announcement.Announcement;
import com.matkam.serwisogloszen.model.announcement.AnnouncementStatus;
import com.matkam.serwisogloszen.repository.AnnouncementRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepo announcementRepo;

    public List<Announcement> findAllAnnouncements() {
        return announcementRepo.findAll();
    }

    public Announcement findAnnouncementById(Long id) {
        return announcementRepo.findById(id)
                .orElseThrow(() -> new AnnouncementNotFountException("Announcement with id " + id + " was not found"));
    }

    public void saveAnnouncement(Announcement announcement) {
        announcementRepo.save(announcement);
    }

    public void deleteAnnouncement(Announcement announcement) {
        announcementRepo.delete(announcement);
    }

    public void deleteAnnouncementById(Long id) {
        announcementRepo.deleteById(id);
    }

    public List<Announcement> findByStatus(AnnouncementStatus status){
        return announcementRepo.findByStatusEquals(status);
    };
}
