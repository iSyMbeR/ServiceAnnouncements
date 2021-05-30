package com.matkam.serwisogloszen.service;

import com.matkam.serwisogloszen.exceptions.UserNotFountException;
import com.matkam.serwisogloszen.model.user.UserApp;
import com.matkam.serwisogloszen.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAppService {
    private final UserRepo userRepo;

    public List<UserApp> findAllUsers() {
        return userRepo.findAll();
    }

    public UserApp findUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFountException("User with id " + id + " was not found"));
    }
    public UserApp findUserByUsername(String name) {
        return userRepo.findUserByUsername(name)
                .orElseThrow(() -> new UserNotFountException("User with name " + name + " was not found"));
    }
    public void saveUser(UserApp user) {
        userRepo.save(user);
    }

    public void deleteUser(UserApp user) {
        userRepo.delete(user);
    }

    public void deleteUserById(Long id) {
        userRepo.deleteById(id);
    }
}
