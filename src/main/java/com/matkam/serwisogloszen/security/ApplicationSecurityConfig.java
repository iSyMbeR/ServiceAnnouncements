package com.matkam.serwisogloszen.security;

import com.matkam.serwisogloszen.model.user.User;
import com.matkam.serwisogloszen.model.user.UserRole;
import com.matkam.serwisogloszen.repository.UserRepo;
import com.matkam.serwisogloszen.service.UserDetailsServiceImplementation;
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
                .username("ja")
                .email("kamil.lobas@gmail.com")
                .password(passwordEncoderConfig.passwordEncoder().encode("ja"))
                .role(UserRole.USER)
                .build();
        User admin = new User("ty", passwordEncoderConfig.passwordEncoder().encode("ty"), UserRole.ADMIN);
        User moderator = new User("ona", passwordEncoderConfig.passwordEncoder().encode("ona"), UserRole.MODERATOR);
        if (userRepo.findUserByUsername(user.getUsername()).isEmpty() && userRepo.findUserByUsername(admin.getUsername()).isEmpty()) {
            userRepo.save(user);
            userRepo.save(admin);
            userRepo.save(moderator);
        }
    }

}
