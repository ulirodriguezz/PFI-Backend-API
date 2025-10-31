package com.example.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String targetEmail, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(targetEmail);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom("ulirodrigueze@gmail.com");

        mailSender.send(email);
       System.out.println("Email sent "+targetEmail);
    }
}
