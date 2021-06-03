package com.matkam.serwisogloszen.repository;

import com.matkam.serwisogloszen.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
}
