package com.matkam.ServiceAnnouncements.service;

import com.matkam.ServiceAnnouncements.model.user.User;
import com.matkam.ServiceAnnouncements.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final UserRepo userRepo;
    public static User LOGGED_USER;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImplementation.class);

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        LOGGER.info("Loading user " + s);
        if (userRepo.findUserByUsername(s).isPresent()) {
            LOGGED_USER = userRepo.findUserByUsername(s).get();
            LOGGER.info("User " + s + " logged in");
        }
        return LOGGED_USER;
    }
}
