package com.matkam.ServiceAnnouncements.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendMailService.class);
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String to, String body, String topic) {
        LOGGER.info("Sending mail..");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("serviceannouncementsjava@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(topic);
        simpleMailMessage.setText(body);
        javaMailSender.send(simpleMailMessage);
        LOGGER.info("Mail sent");
    }
}
