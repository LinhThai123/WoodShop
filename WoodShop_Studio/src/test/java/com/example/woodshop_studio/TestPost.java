package com.example.woodshop_studio;

import com.example.woodshop_studio.entity.Category;
import com.example.woodshop_studio.entity.Post;
import com.example.woodshop_studio.entity.User;
import com.example.woodshop_studio.repository.PostRepository;
import com.example.woodshop_studio.repository.UserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;


@SpringBootTest
public class TestPost {
    @Autowired
    private PostRepository postRepository ;

    @Autowired
    private UserRepository userRepository ;

    @Test
    public void createPost () {
        Random random = new Random() ;
        Faker faker = new Faker() ;
        List<User> users = userRepository.findAll() ;
        for (int i = 0; i < 5; i++) {
            User user = users.get(random.nextInt(users.size()));
            Post post = Post.builder()
                    .title(faker.name().title())
                    .content(faker.lorem().sentence())
                    .description(faker.lorem().sentence())
                    .status(1)
                    .thumbnail(faker.company().logo())
                    .slug(faker.name().lastName())
                    .createdBy(user)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .modifiedAt(new Timestamp(System.currentTimeMillis()))
                    .modifiedBy(user)
                    .build();
            postRepository.save(post);
        }
    }
}
