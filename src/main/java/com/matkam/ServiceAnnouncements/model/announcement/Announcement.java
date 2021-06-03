package com.matkam.ServiceAnnouncements.model.announcement;

import com.matkam.ServiceAnnouncements.model.AbstractModel;
import com.matkam.ServiceAnnouncements.model.Category;
import com.matkam.ServiceAnnouncements.model.user.User;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Announcement extends AbstractModel {
    private String content;
    @OneToOne
    private Category category;
    @OneToOne
    private User user;
    private AnnouncementStatus status;
}
