package com.matkam.serwisogloszen.service;

import com.matkam.serwisogloszen.exceptions.UserNotFountException;
import com.matkam.serwisogloszen.model.user.User;
import com.matkam.serwisogloszen.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAppService {
    private final UserRepo userRepo;

    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    public User findUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFountException("User with id " + id + " was not found"));
    }

    public User findUserByUsername(String name) {
        return userRepo.findUserByUsername(name)
                .orElseThrow(() -> new UserNotFountException("User with name " + name + " was not found"));
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }

    public void deleteUser(User user) {
        userRepo.delete(user);
    }

    public void deleteUserById(Long id) {
        userRepo.deleteById(id);
    }

}
