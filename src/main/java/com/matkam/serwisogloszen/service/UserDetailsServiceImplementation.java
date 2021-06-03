package com.matkam.serwisogloszen.service;

import com.matkam.serwisogloszen.model.user.User;
import com.matkam.serwisogloszen.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final UserRepo userRepo;
    public static User LOGGED_USER;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (userRepo.findUserByUsername(s).isPresent())
            LOGGED_USER = userRepo.findUserByUsername(s).get();
        return LOGGED_USER;
    }
}
