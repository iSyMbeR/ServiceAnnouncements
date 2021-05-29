package com.matkam.serwisogloszen.repository;

import com.matkam.serwisogloszen.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepo extends JpaRepository<Announcement, Long> {
}
