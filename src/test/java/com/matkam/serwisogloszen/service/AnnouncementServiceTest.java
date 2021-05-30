package com.matkam.serwisogloszen.service;

import com.matkam.serwisogloszen.model.announcement.Announcement;
import com.matkam.serwisogloszen.model.Category;
import com.matkam.serwisogloszen.model.user.UserApp;
import com.matkam.serwisogloszen.model.user.UserRole;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@SpringBootTest
//czyszczenie bazy po kazdym
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor
class AnnouncementServiceTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserAppService userService;
    @Autowired
    private AnnouncementService announcementService;


    @BeforeEach
    void fillDataBase() {
        List<String> categoryNames = Arrays.asList("All", "Arts & Crafts", "I dont know");
        categoryNames.forEach(s -> {
            categoryService.saveCategory(Category.builder()
                    .name(s)
                    .build());
        });
        UserApp userApp = UserApp.builder()
                .role(UserRole.USER)
                .username("Mieczyslaw")
                .password("karwasztwarz")
                .announcementList(null)
                .build();
        userService.saveUser(userApp);

        for (int i = 0; i < 3; i++) {
            Announcement announcement = Announcement.builder().user(userApp)
                    .content(RandomString.make(30))
                    .category(categoryService.findCategoriesById((long) ThreadLocalRandom.current().nextInt(1, categoryNames.size() + 1)))
                    .build();
            userApp.addAnnouncement(announcement);
            announcementService.saveAnnouncement(announcement);
        }
        userService.saveUser(userApp);
        assert userService.findAllUsers() != null && announcementService.findAllAnnouncements() != null && categoryService.findAllCategories() != null;
    }

    @Test
    void shouldReturnAllAnnouncements() {
        List<Announcement> announcements = announcementService.findAllAnnouncements();
        assert announcements.size() == 3;
    }

    @Test
    void shouldAddNewAnnouncementToDatabase() {
        announcementService.saveAnnouncement(Announcement.builder().build());
        List<Announcement> announcements = announcementService.findAllAnnouncements();
        assert announcements.size() == 4;
    }

    @Test
    void shouldDeleteLastAnnouncementFromDatabase(){
        List<Announcement> announcements = announcementService.findAllAnnouncements();
        UserApp user = userService.findUserById(1L);
        Announcement ann = announcements.get(announcements.size() - 1);
        user.deleteAnnouncement(ann);
        userService.saveUser(user);
        announcementService.deleteAnnouncement(ann);
        Assertions.assertEquals(announcements.size() - 1, announcementService.findAllAnnouncements().size());
    }

    @Test
    void shouldDeleteAllAnnouncementsAfterDeleteTheirOwner() {
        userService.deleteUserById(1L);
        Assertions.assertEquals(0, announcementService.findAllAnnouncements().size());
    }

}
