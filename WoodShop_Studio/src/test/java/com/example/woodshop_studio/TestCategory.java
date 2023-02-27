package com.example.woodshop_studio;

import com.example.woodshop_studio.entity.Category;
import com.example.woodshop_studio.entity.Product;
import com.example.woodshop_studio.repository.CategoryRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestCategory {

    @Autowired private CategoryRepository categoryRepository ;


    // create category
    @Test
    @Rollback(value = false)
    public void createCategory () {
        Random random = new Random() ;
        Faker faker = new Faker() ;
        for (int i = 0; i < 5; i++) {
            Category category = Category.builder().name(faker.name().fullName())
                    .slug(faker.name().lastName())
                    .build();
            categoryRepository.save(category);
        }
    }

    // find category
    @Test
    @Rollback(value = false)
    public void findCategory () {
       Category category = categoryRepository.findByName("Trang");
        assertThat(category.getName()).isEqualTo("Trang");
    }

    // get list Category
    @Test
    public void testListCategory() {
        List<Category> categories = (List<Category>) categoryRepository.findAll();
        assertThat(categories).size().isGreaterThan(0);
    }

    @Test
    @Rollback(false)
    public void testDeleteCategory() {
        Category category = categoryRepository.findByName("Trang");

        categoryRepository.deleteById(category.getId());

        Category deletedCategory = categoryRepository.findByName("Trang");

        assertThat(deletedCategory).isNull();
    }


}
