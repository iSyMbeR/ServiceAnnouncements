package com.matkam.ServiceAnnouncements.security;

import com.matkam.ServiceAnnouncements.model.user.User;
import com.matkam.ServiceAnnouncements.model.user.UserRole;
import com.matkam.ServiceAnnouncements.repository.UserRepo;
import com.matkam.ServiceAnnouncements.service.UserDetailsServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@RequiredArgsConstructor
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final UserDetailsServiceImplementation userDetailsServiceImplementation;
    private final UserRepo userRepo;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImplementation);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/announcements-admin").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.MODERATOR.name())
                .antMatchers("/announcements-admin/*").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.MODERATOR.name())
                .antMatchers("/announcements").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.MODERATOR.name(), UserRole.USER.name())
                .antMatchers("/announcements/*").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.MODERATOR.name(), UserRole.USER.name())
                .antMatchers("/register").permitAll()
                .antMatchers("/login").permitAll()
                .and()
                .formLogin().permitAll()
                .and()
                .csrf().disable();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void get() {
        User user = User.builder()
                .username("kamil")
                .email("kamil.lobas@gmail.com")
                .password(passwordEncoderConfig.passwordEncoder().encode("kamil"))
                .role(UserRole.USER)
                .build();
        User admin = new User("admin", passwordEncoderConfig.passwordEncoder().encode("admin"), UserRole.ADMIN);
        User moderator = new User("moderator", passwordEncoderConfig.passwordEncoder().encode("moderator"), UserRole.MODERATOR);
        if (userRepo.findUserByUsername(user.getUsername()).isEmpty() && userRepo.findUserByUsername(admin.getUsername()).isEmpty()) {
            userRepo.save(user);
            userRepo.save(admin);
            userRepo.save(moderator);
        }
    }

}
