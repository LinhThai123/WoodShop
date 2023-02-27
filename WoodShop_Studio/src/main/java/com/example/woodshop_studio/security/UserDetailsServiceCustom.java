package com.example.woodshop_studio.security;

import com.example.woodshop_studio.entity.User;
import com.example.woodshop_studio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceCustom implements UserDetailsService {

    @Autowired
    private UserRepository userRepository ;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       User user = userRepository.findByEmail(email);
        if(user != null) {
            return new CustomUserDetails(user);
        }
        else {
            throw new UsernameNotFoundException("User get name "+ email + "does not exites");
        }
    }
}
