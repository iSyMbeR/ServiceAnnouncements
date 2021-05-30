package com.matkam.serwisogloszen.model;

import com.matkam.serwisogloszen.model.Enum.AnnouncementStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Announcement extends AbstractModel {
    private String content;
    @OneToOne
    private Category category;
    @OneToOne
    private UserApp user;
    private AnnouncementStatus status;
}
