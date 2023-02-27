package com.example.woodshop_studio;

import com.example.woodshop_studio.entity.User;
import com.example.woodshop_studio.repository.UserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Collections;

@SpringBootTest
public class TestUser {
    @Autowired
    private UserRepository userRepository ;

    @Test
    public void createUser() {
        Faker faker = new Faker() ;

        for (int i = 0; i < 3; i++) {
            User user = User.builder().name(faker.name().fullName()).address(faker.address().fullAddress())
                    .email(faker.name().lastName())
                    .avatar(faker.avatar().image())
                    .phone(faker.phoneNumber().phoneNumber())
                    .password(faker.phoneNumber().cellPhone())
                    .status(true)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .modifiedAt(new Timestamp(System.currentTimeMillis()))
                    .roles(Collections.singletonList("USER"))
                    .build();
            userRepository.save(user);
        }
    }
}
