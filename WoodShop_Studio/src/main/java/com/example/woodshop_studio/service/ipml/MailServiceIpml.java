package com.example.woodshop_studio.service.ipml;

import com.example.woodshop_studio.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailServiceIpml implements MailService {

    @Autowired
    private JavaMailSender emailSender ;

    @Override
    public void send(String email, String subject, String content) {
        // create a simple mail message
        SimpleMailMessage mailMessage = new SimpleMailMessage() ;

        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);

        // send mail
        emailSender.send(mailMessage);
    }
}
