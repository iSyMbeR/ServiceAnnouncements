package com.matkam.ServiceAnnouncements.model.announcement;

import com.matkam.ServiceAnnouncements.model.AbstractModel;
import com.matkam.ServiceAnnouncements.model.Category;
import com.matkam.ServiceAnnouncements.model.user.User;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

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
    @Null
    private LocalDateTime finishDate;

    public Announcement(String content, Category category, User user, AnnouncementStatus status) {
        this.content = content;
        this.category = category;
        this.user = user;
        this.status = status;
    }
}
