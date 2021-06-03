package com.matkam.serwisogloszen.controller;

import com.matkam.serwisogloszen.service.SendMailService;
import com.matkam.serwisogloszen.service.UserAppService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route("gui")
public class MainGui extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainGui.class);

    public MainGui(UserAppService userAppService, SendMailService sendMailService) {
        LOGGER.info("Connected with MainGui");
        Label label = new Label("siema");
        Button button = new Button("Kliknij");
        button.addClickListener(buttonClickEvent -> {
            label.setText(userAppService.findUserById(1L).getUsername());
            sendMailService.sendMail("andrzejduda@gmail.com", "Nie pytaj mnie o imie walczac z ostrym cieniem mgly", "Tescik");
            LOGGER.info("Message to " + "andrzejduda@gmail.com" + "has been sent");
        });
        setHorizontalComponentAlignment(Alignment.CENTER, label);
        add(label, button);
    }
}
