package com.matkam.ServiceAnnouncements.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.annotation.PreDestroy;
import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
public class AbstractModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    private Date created;
    @JsonIgnore
    private Date updated;
    @JsonIgnore
    private Date deleted;

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }

    @PreDestroy
    protected void onDelete() {
        deleted = new Date();
    }
}
