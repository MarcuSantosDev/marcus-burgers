
package com.marcusantosproject.backend;

import com.marcusantosproject.backend.entities.Category;
import com.marcusantosproject.backend.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository repository;

    @Test
    void shouldSaveCategory() {
        Category category = new Category(
                "Hambúrgueres",
                "Hambúrgueres artesanais"
        );

        Category saved = repository.save(category);

        assertNotNull(saved.getCategoryId());
    }
}
