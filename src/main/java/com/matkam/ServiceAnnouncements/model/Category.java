package com.matkam.ServiceAnnouncements.model;

import lombok.*;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Category extends AbstractModel {
    private String name;
}
