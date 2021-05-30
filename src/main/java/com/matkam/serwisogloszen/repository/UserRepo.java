package com.matkam.serwisogloszen.repository;

import com.matkam.serwisogloszen.model.user.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserApp, Long> {
    Optional<UserApp> findUserByUsername(String username);
}
