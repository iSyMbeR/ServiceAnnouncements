package com.matkam.serwisogloszen.repository;

import com.matkam.serwisogloszen.model.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserApp, Long> {
    UserApp findUserByUsername(String username);
}
