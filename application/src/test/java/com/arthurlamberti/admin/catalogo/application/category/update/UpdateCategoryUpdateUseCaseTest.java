package com.arthurlamberti.admin.catalogo.application.category.update;

import com.arthurlamberti.admin.catalogo.domain.category.Category;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryGateway;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryId;
import com.arthurlamberti.admin.catalogo.domain.exceptions.DomainException;
import com.arthurlamberti.admin.catalogo.domain.validation.handler.NotificationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUpdateUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase updateCategoryUpdateUseCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    //1. teste caminho feliz
    //2. teste passando propriedade invalida
    //3. tste atualizando categoria para inativa
    //4. teste simulando erro generico vindo do gateway
    //5. atualizar categoria passando id invalido

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_thenReturnCategoryId() {
        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedName = "Movies";
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var command = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(Category.clone(aCategory)));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        var actualOutPut = updateCategoryUpdateUseCase.execute(command).get();
        assertNotNull(actualOutPut);
        assertNotNull(actualOutPut.id());

        verify(categoryGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(1)).update(argThat(
                aUpdateCategory -> Objects.equals(expectedName, aUpdateCategory.getName())
                        && Objects.equals(expectedDescription, aUpdateCategory.getDescription())
                        && Objects.equals(expectedIsActive, aUpdateCategory.isActive())
                        && Objects.equals(expectedId, aUpdateCategory.getId())
                        && Objects.equals(aCategory.getCreatedAt(), aUpdateCategory.getCreatedAt())
                        && aCategory.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt())
                        && Objects.isNull(aUpdateCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenReturnDomainException() {
        final var aCategory = Category.newCategory("Film", null, true);

        final String expectedName = null;
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(Category.clone(aCategory)));

        NotificationHandler notificationHandler = updateCategoryUpdateUseCase.execute(aCommand).getLeft();

        assertEquals(expectedMessage, notificationHandler.firstError().message());
        assertEquals(expectedErrorCount, notificationHandler.getErrors().size());

        verify(categoryGateway, times(0)).update(any());
    }

    @Test
    public void givenAValidInactiveCommandCategory_whenCallsUpdateCategory_thenReturnInactiveCategoryId() {
        final var aCategory =
                Category.newCategory("Film", null, true);

        final var expectedName = "Movies";
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var command = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(Category.clone(aCategory)));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        var actualOutPut = updateCategoryUpdateUseCase.execute(command).get();
        assertNotNull(actualOutPut);
        assertNotNull(actualOutPut.id());

        verify(categoryGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(1)).update(argThat(
                aUpdateCategory -> Objects.equals(expectedName, aUpdateCategory.getName())
                        && Objects.equals(expectedDescription, aUpdateCategory.getDescription())
                        && Objects.equals(expectedIsActive, aUpdateCategory.isActive())
                        && Objects.equals(expectedId, aUpdateCategory.getId())
                        && Objects.equals(aCategory.getCreatedAt(), aUpdateCategory.getCreatedAt())
                        && aCategory.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt())
                        && Objects.nonNull(aUpdateCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsAException_thenReturnAException() {
        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedError = "GatewayError";
        final var expectedErrorCount = 1;
        final var expectedName = "Movies";
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var command =
                UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(Mockito.eq(expectedId)))
//                .thenThrow(new IllegalStateException("expectedError"));
                .thenReturn(Optional.of(Category.clone(aCategory)));

        when(categoryGateway.update(any()))
//                .thenAnswer(returnsFirstArg());
                .thenThrow(new IllegalStateException(expectedError));

        var notification = updateCategoryUpdateUseCase.execute(command).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedError, notification.firstError().message());

//        verify(categoryGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(1)).update(argThat(
                aUpdateCategory -> Objects.equals(expectedName, aUpdateCategory.getName())
                        && Objects.equals(expectedDescription, aUpdateCategory.getDescription())
                        && Objects.equals(expectedIsActive, aUpdateCategory.isActive())
                        && Objects.equals(expectedId, aUpdateCategory.getId())
                        && Objects.equals(aCategory.getCreatedAt(), aUpdateCategory.getCreatedAt())
                        && aCategory.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt())
                        && Objects.isNull(aUpdateCategory.getDeletedAt())
        ));
    }
    @Test
    public void givenAnInvalidId_whenCallsUpdateCategory_thenShouldReturnNotFoundException() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId =  "123";

        final var command = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(Mockito.eq(CategoryId.from(expectedId))))
                .thenReturn(Optional.empty());


        final var actualException = assertThrows(DomainException.class, () -> updateCategoryUpdateUseCase.execute(command));

        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(categoryGateway, times(1)).findById(CategoryId.from(expectedId));
        verify(categoryGateway, times(0)).update(any());
    }

}
