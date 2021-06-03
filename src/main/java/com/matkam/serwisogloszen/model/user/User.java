package com.matkam.serwisogloszen.model.user;

import com.matkam.serwisogloszen.model.AbstractModel;
import com.matkam.serwisogloszen.model.announcement.Announcement;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class User extends AbstractModel implements UserDetails {
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Email
    private String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Announcement> announcementList;

    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void addAnnouncement(Announcement announcement) {
        if (announcementList == null)
            announcementList = new ArrayList<>();

        announcementList.add(announcement);
    }

    public void deleteAnnouncement(Announcement announcement) {
        if (announcementList != null)
            announcementList.remove(announcement);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
