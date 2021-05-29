package com.matkam.serwisogloszen.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Data
public class Announcement extends AbstractModel {
    private String content;
    @OneToOne
    private Category category;
}
