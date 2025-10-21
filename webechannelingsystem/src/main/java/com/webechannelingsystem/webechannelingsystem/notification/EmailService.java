package com.webechannelingsystem.webechannelingsystem.notification;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


// Singleton class EmailService
public class EmailService {

    private static EmailService instance;
    private JavaMailSender javaMailSender;

    private EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public static synchronized EmailService getInstance(JavaMailSender javaMailSender) {
        if (instance == null) {
            instance = new EmailService(javaMailSender);
        }
        return instance;
    }

    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
}
