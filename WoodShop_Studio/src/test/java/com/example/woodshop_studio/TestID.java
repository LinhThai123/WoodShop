package com.example.woodshop_studio;

import com.example.woodshop_studio.entity.Category;
import com.example.woodshop_studio.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestID {

    @Autowired
    CategoryRepository categoryRepository ;

    @Autowired
     private TestEntityManager entityManager ;

    @Test
    public void testId () {
        Category category = new Category();
        category.setName("Linh");
        String id = (String) entityManager.persistAndGetId(category);
        assertThat(id).hasSizeGreaterThanOrEqualTo(12);
    }
}
