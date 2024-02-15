package com.arthurlamberti.admin.catalogo.application.category.update;

import com.arthurlamberti.admin.catalogo.IntegrationTest;
import com.arthurlamberti.admin.catalogo.domain.category.Category;
import com.arthurlamberti.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.arthurlamberti.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@IntegrationTest
public class UpdateCategoryUpdateUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase updateCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_thenReturnCategoryId() {
        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedName = "Movies";
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var command = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        save(aCategory);
        assertEquals(1, categoryRepository.count());

        var actualOutPut = updateCategoryUseCase.execute(command).get();
        var actualCategory = categoryRepository.findById(actualOutPut.id()).get();

        assertEquals(1, categoryRepository.count());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNull(actualCategory.getDeletedAt());

    }

    private void save(final Category... category) {
        categoryRepository.saveAllAndFlush(Arrays.stream(category).map(CategoryJpaEntity::from).toList());
    }
}
