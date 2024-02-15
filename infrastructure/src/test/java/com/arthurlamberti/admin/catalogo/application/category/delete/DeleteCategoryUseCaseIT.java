package com.arthurlamberti.admin.catalogo.application.category.delete;

import com.arthurlamberti.admin.catalogo.IntegrationTest;
import com.arthurlamberti.admin.catalogo.domain.category.Category;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryGateway;
import com.arthurlamberti.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.arthurlamberti.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DefaultDeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOk() {
        final var aCategory = Category.newCategory("Film", null, true);
        final var expectedId = aCategory.getId();
        assertEquals(0,categoryRepository.count());
        save(aCategory);
        assertEquals(1,categoryRepository.count());

        useCase.execute(expectedId.getValue());
        assertEquals(0,categoryRepository.count());
    }


    private void save(final Category... category) {
        categoryRepository.saveAllAndFlush(Arrays.stream(category).map(CategoryJpaEntity::from).toList());
    }
}
