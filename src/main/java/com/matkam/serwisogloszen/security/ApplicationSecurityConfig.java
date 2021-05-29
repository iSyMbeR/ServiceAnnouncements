package com.matkam.serwisogloszen.security;

import com.matkam.serwisogloszen.model.UserApp;
import com.matkam.serwisogloszen.repository.UserRepo;
import com.matkam.serwisogloszen.service.UserDetailsServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImplementation userDetailsServiceImplementation;
    private final UserRepo userRepo;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImplementation);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/all").hasAnyAuthority("USER")
                .antMatchers("/home").hasAnyAuthority("ADMIN")
                .antMatchers("/").permitAll()
                .and()
                .formLogin().permitAll()
                .and()
                .csrf().disable();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void get() {
        UserApp appUserUser = new UserApp("ja", passwordEncoder().encode("ja"), "USER");
        UserApp appUserAdmin = new UserApp("ty", passwordEncoder().encode("ty"), "ADMIN");
        if (userRepo.findUserByUsername(appUserUser.getUsername()) == null && userRepo.findUserByUsername(appUserAdmin.getUsername()) == null){
            userRepo.save(appUserUser);
            userRepo.save(appUserAdmin);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
