package com.example.woodshop_studio.service;

import org.springframework.stereotype.Service;

@Service
public interface MailService {

    public void send (String email , String subject , String content);
}
