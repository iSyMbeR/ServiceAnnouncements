package com.matkam.serwisogloszen.model;

import lombok.*;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Category extends AbstractModel {
    private String name;
}
