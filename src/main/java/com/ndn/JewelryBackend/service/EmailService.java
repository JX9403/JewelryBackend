package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("spring.mail.username")
    private String fromEmail;

    public void sendConfirmationCode(User user){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Confirm your email");
//        message.setText("Please confirm your email by entering this code " + user.getConfirmationCode());

        mailSender.send(message);
    }
}
