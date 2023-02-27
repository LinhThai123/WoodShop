package com.example.woodshop_studio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthorizationFilterCustom extends OncePerRequestFilter {

    @Autowired
    UserDetailsServiceCustom userDetailsServiceCustom ;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Lấy ra thông tin trong session
        String userEmail = (String) request.getSession().getAttribute("MY_SESSION");

        // Kiểm tra userEmail
        if(userEmail == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy thông tin của user dựa trên email
        CustomUserDetails user = (CustomUserDetails) userDetailsServiceCustom.loadUserByUsername(userEmail);

        // Tạo đối tượng authentication
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, userDetailsServiceCustom.loadUserByUsername(userEmail).getAuthorities());

        // Lưu vào trong context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
