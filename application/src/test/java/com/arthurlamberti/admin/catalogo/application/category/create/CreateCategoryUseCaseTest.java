package com.arthurlamberti.admin.catalogo.application.category.create;

import com.arthurlamberti.admin.catalogo.domain.category.CategoryGateway;
import com.arthurlamberti.admin.catalogo.domain.validation.handler.NotificationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase defaultCreateCategoryUseCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    //1. teste caminho feliz
    //2. teste passando propriedade invalida
    //3. tste criando categoria inativa
    //4. teste simulando erro generico vindo do gateway

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_thenReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;
        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = defaultCreateCategoryUseCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).create(argThat(
                aCategory -> Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectedDescription, aCategory.getDescription())
                        && Objects.equals(expectedIsActive, aCategory.isActive())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.isNull(aCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        NotificationHandler notificationHandler = defaultCreateCategoryUseCase.execute(aCommand).getLeft();

        assertEquals(expectedMessage, notificationHandler.firstError().message());
        assertEquals(expectedErrorCount, notificationHandler.getErrors().size());

        verify(categoryGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_thenReturnInactiveCategoryId() {
        final String expectedName = "Movies";
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = false;
        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = defaultCreateCategoryUseCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).create(argThat(
                aCategory -> Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectedDescription, aCategory.getDescription())
                        && Objects.equals(expectedIsActive, aCategory.isActive())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.nonNull(aCategory.getDeletedAt())
        ));
    }


    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_thenReturnDomainException() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;
        final var expectedMessage = "Gateway error";
        final var expectedErrorCount = 1;
        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(any())).thenThrow(new IllegalStateException(expectedMessage));

        NotificationHandler notificationHandler = defaultCreateCategoryUseCase.execute(aCommand).getLeft();

        assertEquals(expectedMessage, notificationHandler.firstError().message());
        assertEquals(expectedErrorCount, notificationHandler.getErrors().size());

        verify(categoryGateway, times(1)).create(argThat(
                aCategory -> Objects.equals(expectedName, aCategory.getName())
                        && Objects.equals(expectedDescription, aCategory.getDescription())
                        && Objects.equals(expectedIsActive, aCategory.isActive())
                        && Objects.nonNull(aCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && Objects.nonNull(aCategory.getUpdatedAt())
                        && Objects.isNull(aCategory.getDeletedAt())
        ));
    }
}
