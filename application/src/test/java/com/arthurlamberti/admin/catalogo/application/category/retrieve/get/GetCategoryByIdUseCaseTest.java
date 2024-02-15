package com.arthurlamberti.admin.catalogo.application.category.retrieve.get;

import com.arthurlamberti.admin.catalogo.domain.category.Category;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryGateway;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryId;
import com.arthurlamberti.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase usecase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldBeReturnCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;
        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId();

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        final var actualCategory = usecase.execute(expectedId.getValue());
        assertEquals(CategoryOutPut.from(aCategory), actualCategory);
        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedIsActive, actualCategory.isActive());
    }

    @Test
    public void givenAnInvalidId_whenCallsGetCategory_shouldBeReturnNotFound() {
        final var expectedError = "";
        final var expectedErrorCount = 1;
        final var expectedId = CategoryId.from("123");

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> usecase.execute(expectedId.getValue())
        );
        assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsError_shouldReturnException() {
        final var expectedError = "Gateway error";
        final var expectedErrorCount = 1;
        final var expectedId = CategoryId.from("123");

        when(categoryGateway.findById(eq(expectedId)))
                .thenThrow(new IllegalStateException(expectedError));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> usecase.execute(expectedId.getValue())
        );
        assertEquals(expectedError, actualException.getMessage());
    }

}
