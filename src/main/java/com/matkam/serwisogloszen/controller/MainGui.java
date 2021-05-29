package com.matkam.serwisogloszen.controller;

import com.matkam.serwisogloszen.service.UserAppService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("gui")
public class MainGui extends VerticalLayout {

    public MainGui(UserAppService userAppService) {
        Label label = new Label("siema");
        Button button = new Button("Kliknij");
        button.addClickListener(buttonClickEvent -> {
            label.setText(userAppService.findUserById(1L).getUsername());
        });
        setHorizontalComponentAlignment(Alignment.CENTER, label);
        add(label, button);
    }
}
