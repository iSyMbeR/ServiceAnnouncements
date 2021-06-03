package com.matkam.serwisogloszen.controller;

import com.matkam.serwisogloszen.service.UserAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class HomeController {
    private final UserAppService userAppService;

    @GetMapping("/home")
    public String getUserById() {
        return "siema w domu";
    }

    @GetMapping("/home/{id}")
    public String getUserById(@PathVariable("id") Long id) {
        return userAppService.findUserById(id).getUsername();
    }
}
