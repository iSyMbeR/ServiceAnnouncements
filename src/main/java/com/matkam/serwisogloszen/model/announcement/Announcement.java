package com.matkam.serwisogloszen.model.announcement;

import com.matkam.serwisogloszen.model.AbstractModel;
import com.matkam.serwisogloszen.model.Category;
import com.matkam.serwisogloszen.model.user.User;
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
