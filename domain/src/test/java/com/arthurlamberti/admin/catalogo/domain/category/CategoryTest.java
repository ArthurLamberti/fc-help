package com.arthurlamberti.admin.catalogo.domain.category;

import com.arthurlamberti.admin.catalogo.domain.exceptions.DomainException;
import com.arthurlamberti.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    @Test
    public void givenAValidParams_WhenCallNewCategory_thenInstantiateACategory() {

        final var expectedName = "Movies";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_theSouldReceiveError() {

        final String expectedName = null;
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var err = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));
        assertEquals(expectedErrorMessage, err.getErrors().get(0).message());
        assertEquals(1, err.getErrors().size());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_theSouldReceiveError() {

        final var expectedName = "   ";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var err = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));
        assertEquals(expectedErrorMessage, err.getErrors().get(0).message());
        assertEquals(1, err.getErrors().size());
    }

    @Test
    public void givenAnInvalidNameLengthLessThen3_whenCallNewCategoryAndValidate_theSouldReceiveError() {

        final var expectedName = "ab ";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var err = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));
        assertEquals(expectedErrorMessage, err.getErrors().get(0).message());
        assertEquals(1, err.getErrors().size());
    }

    @Test
    public void givenAnInvalidNameLengthMoreThen255_whenCallNewCategoryAndValidate_theSouldReceiveError() {

        final var expectedName = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas gravida enim ut iaculis lacinia. " +
                "Praesent venenatis massa justo, non tempus risus fringilla quis. Nulla mi odio, rhoncus eu finibus nec, venenatis sed est. " +
                "Maecenas vitae mi magna. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var err = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));
        assertEquals(expectedErrorMessage, err.getErrors().get(0).message());
        assertEquals(1, err.getErrors().size());
    }


    @Test
    public void givenAnEmptyDescription_WhenCallNewCategory_thenInstantiateACategory() {

        final var expectedName = "Movies";
        final var expectedDescription = " ";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertNotNull(actualCategory);
        assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidIsActiveFalse_WhenCallNewCategory_thenInstantiateACategory() {

        final var expectedName = "Movies";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = false;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallDeactivate_thenReturnCategoryInactive(){

        final var expectedName = "Movies";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = false;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, true);
        assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();

        assertNull(aCategory.getDeletedAt());
        assertTrue(aCategory.isActive());

        final var actualCategory = aCategory.deactivate();
        assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        assertEquals(aCategory.getId(),actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAVInactiveCategory_whenCallActivate_thenReturnCategoryInactive(){

        final var expectedName = "Movies";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, false);
        assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();

        assertNotNull(aCategory.getDeletedAt());
        assertFalse(aCategory.isActive());

        final var actualCategory = aCategory.activate();
        assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        assertEquals(aCategory.getId(),actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdate_thenReturnCategoryUpdated() {


        final var expectedName = "Movies";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Movie", "Most view", expectedIsActive);
        assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();

        var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        assertEquals(aCategory.getId(),actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCUpdateToInactive_thenReturnCategoryUpdated() {


        final var expectedName = "Movies";
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = false;

        final var aCategory = Category.newCategory("Movie", "Most view", true);
        assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();

        var actualCategory = aCategory.update(expectedName, expectedDescription, false);

        assertEquals(aCategory.getId(),actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNotNull(actualCategory.getDeletedAt());
    }


    @Test
    public void givenAValidCategory_whenCallUpdateWithInvalidParams_thenReturnCategoryUpdated() {


        final String expectedName = null;
        final var expectedDescription = "Most viewed category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Movie", "Most view", true);

        final var updatedAt = aCategory.getUpdatedAt();

        var actualCategory = aCategory.update(expectedName, expectedDescription, true);

        assertEquals(aCategory.getId(),actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        assertNull(actualCategory.getDeletedAt());
    }
}
