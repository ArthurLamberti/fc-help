package com.arthurlamberti.admin.catalogo.application.category.create;

import com.arthurlamberti.admin.catalogo.IntegrationTest;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryGateway;
import com.arthurlamberti.admin.catalogo.domain.validation.handler.NotificationHandler;
import com.arthurlamberti.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_thenReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;
        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        assertEquals(0, categoryRepository.count());
        final var actualOutput = createCategoryUseCase.execute(aCommand).get();

        assertEquals(1, categoryRepository.count());
        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualCategory = categoryRepository.findById(actualOutput.id()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull( actualCategory.getDeletedAt());
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        assertEquals(0, categoryRepository.count());
        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        NotificationHandler notificationHandler = createCategoryUseCase.execute(aCommand).getLeft();

        assertEquals(expectedMessage, notificationHandler.firstError().message());
        assertEquals(expectedErrorCount, notificationHandler.getErrors().size());

        verify(categoryGateway, times(0)).create(any());
        assertEquals(0, categoryRepository.count());
    }

}
