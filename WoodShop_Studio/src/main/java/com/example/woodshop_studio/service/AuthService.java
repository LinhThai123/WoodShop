package com.example.woodshop_studio.service;

import com.example.woodshop_studio.entity.User;
import com.example.woodshop_studio.model.request.LoginReq;
import com.example.woodshop_studio.model.request.RegisterReq;
import com.example.woodshop_studio.model.request.UserReq;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public interface AuthService {

    public UserReq login (LoginReq req , HttpSession session) ;

    public String register (RegisterReq req );

    public String logout (HttpSession session) ;

    public String generateTokenAndSenEmail (User user) ;
}
