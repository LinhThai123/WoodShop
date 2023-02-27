package com.example.woodshop_studio.controller;

import com.example.woodshop_studio.model.request.LoginReq;
import com.example.woodshop_studio.model.request.RegisterReq;
import com.example.woodshop_studio.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    @Autowired
    private AuthService authService ;

    @PostMapping (value = "signin")
    public ResponseEntity<?> login (@RequestBody LoginReq req , HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            authService.login(req,session);
            return ResponseEntity.ok("Đăng nhập thành công");
        }
        return ResponseEntity.badRequest().body("Đăng nhập thất bại");
    }
    @GetMapping ("logout")
    public String logout (HttpSession session) {
        return authService.logout(session) ;
    }

    @PostMapping( value = "register")
    public ResponseEntity<?> register (@RequestBody RegisterReq req) {
        authService.register(req);
        return ResponseEntity.ok("") ;
    }

}
