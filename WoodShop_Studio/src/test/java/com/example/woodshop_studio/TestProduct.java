package com.example.woodshop_studio;

import com.example.woodshop_studio.entity.Category;
import com.example.woodshop_studio.entity.Product;
import com.example.woodshop_studio.repository.CategoryRepository;
import com.example.woodshop_studio.repository.ProductRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class TestProduct {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void createProduct() {
        Random random = new Random() ;
        Faker faker = new Faker() ;
        List<Category> categories = categoryRepository.findAll();

        for (int i = 0; i < 5; i++) {
            Category category = categories.get(random.nextInt(categories.size()));
            Product product = Product.builder().description(faker.lorem().sentence())
                    .image(faker.company().logo())
                    .name(faker.name().fullName())
                    .slug(faker.name().name())
                    .quantity(50)
                    .price(1000L)
                    .category(category)
                    .status(1)
                    .build();
            productRepository.save(product);
        }
    }

}
