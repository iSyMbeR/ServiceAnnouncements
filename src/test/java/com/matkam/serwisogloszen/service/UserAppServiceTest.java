package com.matkam.serwisogloszen.service;

import com.matkam.serwisogloszen.model.user.UserApp;
import com.matkam.serwisogloszen.model.user.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserAppServiceTest {

    @Autowired
    private UserAppService userAppService;

    @BeforeEach
    void fillDataBase() {
        userAppService.saveUser(UserApp.builder()
                .username("Zbyszek")
                .password("Stonoga")
                .role(UserRole.USER).build());
        userAppService.saveUser(UserApp.builder()
                .username("Smieszek")
                .password("Cieto")
                .role(UserRole.USER).build());
        userAppService.saveUser(UserApp.builder()
                .username("Tata")
                .password("uranskiego")
                .role(UserRole.USER).build());
    }

    @Test
    void shouldFindAllUsers() {
        Assertions.assertEquals(3, userAppService.findAllUsers().size());
    }

    @Test
    void shouldFindUserById() {
        Assertions.assertEquals(3, userAppService.findAllUsers().size());
    }

    @Test
    void shouldFindUserByUsername() {
        Assertions.assertEquals("Zbyszek", userAppService.findUserByUsername("Zbyszek").getUsername());
    }

    @Test
    void shouldSaveUser() {
        userAppService.saveUser(UserApp.builder().build());
        Assertions.assertEquals(4, userAppService.findAllUsers().size());
    }

    @Test
    void shouldDeleteUser() {
        userAppService.deleteUser(userAppService.findUserById(1L));
        Assertions.assertEquals(2, userAppService.findAllUsers().size());
    }

    @Test
    void shouldDeleteUserById() {
        userAppService.deleteUserById(1L);
        Assertions.assertEquals(2, userAppService.findAllUsers().size());
    }
}
