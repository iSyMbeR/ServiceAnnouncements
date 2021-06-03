package com.matkam.ServiceAnnouncements.service;

import com.matkam.ServiceAnnouncements.exceptions.UserNotFountException;
import com.matkam.ServiceAnnouncements.model.user.User;
import com.matkam.ServiceAnnouncements.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAppService {
    private final UserRepo userRepo;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAppService.class);

    public List<User> findAllUsers() {
        LOGGER.info("Getting all users");
        return userRepo.findAll();
    }

    public User findUserById(Long id) {
        LOGGER.info("Getting user by id " + id);
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFountException("User with id " + id + " was not found"));
    }

    public User findUserByUsername(String name) {
        LOGGER.info("Getting user by name " + name);
        return userRepo.findUserByUsername(name)
                .orElseThrow(() -> new UserNotFountException("User with name " + name + " was not found"));
    }

    public void saveUser(User user) {
        userRepo.save(user);
        LOGGER.info("Saved user " + user.getUsername());
    }

    public void deleteUser(User user) {
        userRepo.delete(user);
        LOGGER.info("Deleted user " + user.getUsername());
    }

    public void deleteUserById(Long id) {
        userRepo.deleteById(id);
        LOGGER.info("Saved user with id" + id);
    }

}
