package com.arthurlamberti.admin.catalogo.application.category.retrieve.get;

import com.arthurlamberti.admin.catalogo.IntegrationTest;
import com.arthurlamberti.admin.catalogo.domain.category.Category;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryGateway;
import com.arthurlamberti.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.arthurlamberti.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@IntegrationTest
public class GetCategoryByIdUseCaseTestIT {

    @Autowired
    private GetCategoryByIdUseCase getCategoryByIdUseCase;


    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldBeReturnCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;
        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId();

        assertEquals(0, categoryRepository.count());
        save(aCategory);

        assertEquals(1, categoryRepository.count());

        final var actualCategory = getCategoryByIdUseCase.execute(expectedId.getValue());
        assertEquals(CategoryOutPut.from(aCategory), actualCategory);
        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedIsActive, actualCategory.isActive());
    }

    private void save(final Category... category) {
        categoryRepository.saveAllAndFlush(Arrays.stream(category).map(CategoryJpaEntity::from).toList());
    }


}
