package com.matkam.ServiceAnnouncements.repository;

import com.matkam.ServiceAnnouncements.model.announcement.Announcement;
import com.matkam.ServiceAnnouncements.model.announcement.AnnouncementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnnouncementRepo extends JpaRepository<Announcement, Long> {
    List<Announcement> findByStatusEquals(AnnouncementStatus status);
}
