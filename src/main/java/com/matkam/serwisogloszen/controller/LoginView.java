package com.matkam.serwisogloszen.controller;

import com.matkam.serwisogloszen.model.user.User;
import com.matkam.serwisogloszen.model.user.UserRole;
import com.matkam.serwisogloszen.security.PasswordEncoderConfig;
import com.matkam.serwisogloszen.service.SendMailService;
import com.matkam.serwisogloszen.service.UserAppService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Route("register")
class RegisterView extends Composite {
    private static final String HOST = "localhost";
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterView.class);

    private final PasswordEncoderConfig passwordEncoderConfig;
    private final UserAppService userAppService;
    private final SendMailService sendMailService;

    public RegisterView(UserAppService userAppService, PasswordEncoderConfig passwordEncoderConfig, SendMailService sendMailService) {
        this.userAppService = userAppService;
        this.passwordEncoderConfig = passwordEncoderConfig;
        this.sendMailService = sendMailService;
    }

    @Override
    protected Component initContent() {
        LOGGER.info("New user connected to " + HOST + "/register");
        TextField username = new TextField("Username");
        TextField email = new TextField("Email");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm password");
        return new VerticalLayout(
                new H2("Register"),
                username,
                email,
                password1,
                password2,
                new Button("Send", event -> register(
                        username.getValue(),
                        email.getValue(),
                        password1.getValue(),
                        password2.getValue()
                ))
        );
    }

    private void register(String username, String email, String password1, String password2) {
        LOGGER.info("Trying to register new account...");
        List<User> userList = userAppService.findAllUsers();
        List<String> userNameList = userList.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        if (username.trim().isEmpty()) {
            Notification.show("Enter a username");
        } else if (userNameList.contains(username)) {
            Notification.show("A user with that name already exists");
        } else if (email.isEmpty()) {
            Notification.show("Enter an email");
        } else if (!email.contains("@") || email.length() < 4) {
            Notification.show("Please enter a valid e-mail");
        } else if (password1.isEmpty()) {
            Notification.show("Enter a password");
        } else if (!password1.equals(password2)) {
            Notification.show("Passwords don't match");
        } else {
            userAppService.saveUser(
                    User.builder()
                            .username(username)
                            .password(passwordEncoderConfig.passwordEncoder().encode(password1))
                            .role(UserRole.USER)
                            .email(email)
                            .build()
            );

            String message = "Hello,Thank you for creating an account.\n\nYour account details:" +
                    "\nUsername: " + username +
                    "\nPassword: " + password1 +
                    "\nRole: " + UserRole.USER.name() +
                    "\nEmail: " + email +
                    "\n\nPlease feel free to contact us if you need help: serviceannouncementsjava@gmail.com";

            sendMailService.sendMail(email, message, "ServiceAnnouncements Account");
            Notification.show("Check your email.");
            LOGGER.info("User with username " + username + " has been added");
        }
    }
}
