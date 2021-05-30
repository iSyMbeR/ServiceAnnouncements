package com.matkam.serwisogloszen.repository;

import com.matkam.serwisogloszen.model.announcement.Announcement;
import com.matkam.serwisogloszen.model.announcement.AnnouncementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnnouncementRepo extends JpaRepository<Announcement, Long> {
    List<Announcement> findByStatusEquals(AnnouncementStatus status);
}
