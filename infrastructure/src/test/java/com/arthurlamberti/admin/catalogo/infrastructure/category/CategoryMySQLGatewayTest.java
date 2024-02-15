package com.arthurlamberti.admin.catalogo.infrastructure.category;

import com.arthurlamberti.admin.catalogo.domain.category.Category;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryId;
import com.arthurlamberti.admin.catalogo.domain.category.CategorySearchParam;
import com.arthurlamberti.admin.catalogo.MySQLGatewayTest;
import com.arthurlamberti.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.arthurlamberti.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAValidCategory_whenCallsCreate_thenShouldReturnANewCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertEquals(0, categoryRepository.count());
        final var actualCategory = categoryMySQLGateway.create(aCategory);

        assertEquals(1, categoryRepository.count());

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());

        final var anEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        assertEquals(aCategory.getId().getValue(), anEntity.getId());
        assertEquals(expectedName, anEntity.getName());
        assertEquals(expectedDescription, anEntity.getDescription());
        assertEquals(expectedIsActive, anEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), anEntity.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), anEntity.getUpdatedAt());
        assertNull(anEntity.getDeletedAt());

    }

    @Test
    public void givenAValidCategory_whenCallsUpdate_thenShouldReturnACategoryUpdated() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Muvis", null, expectedIsActive);

        assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        assertEquals(1, categoryRepository.count());

        final var anUpdatedCategory = aCategory.clone().update(expectedName, expectedDescription,  expectedIsActive);
        final var actualCategory = categoryMySQLGateway.update(anUpdatedCategory);

        assertEquals(1, categoryRepository.count());

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNull(actualCategory.getDeletedAt());
        final var anEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        assertEquals(aCategory.getId().getValue(), anEntity.getId());
        assertEquals(expectedName, anEntity.getName());
        assertEquals(expectedDescription, anEntity.getDescription());
        assertEquals(expectedIsActive, anEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), anEntity.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNull(anEntity.getDeletedAt());
    }

    @Test
    public void givenAPrePersistedCategory_whenTryToDeleteIt_shouldDeleteCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        assertEquals(1, categoryRepository.count());

        categoryMySQLGateway.deleteById(aCategory.getId());
        assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAnInvalidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        assertEquals(0, categoryRepository.count());
        categoryMySQLGateway.deleteById(CategoryId.from("iinvalid"));
        assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCaateogryId_whenCallsUpdate_thenShouldReturnACategoryUpdated() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most viewed category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryMySQLGateway.findById(aCategory.getId()).get();

        assertEquals(1, categoryRepository.count());

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        assertEquals(0, categoryRepository.count());
        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchParam(0, 1,"", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(query);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(documentaries.getId(), actualResult.items().get(0).getId());

    }

    @Test
    public void givenEmptyCategories_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        assertEquals(0, categoryRepository.count());

        final var query = new CategorySearchParam(0, 1,"", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(query);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(0, actualResult.items().size());
    }


    @Test
    public void givenFollowPagination_whenCallsFindAll_shouldReturnPagination() {

        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        assertEquals(0, categoryRepository.count());
        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        assertEquals(3, categoryRepository.count());

        var query = new CategorySearchParam(0, 1,"", "name", "asc");
        var actualResult = categoryMySQLGateway.findAll(query);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(documentaries.getId(), actualResult.items().get(0).getId());

        expectedPage =1;
        query = new CategorySearchParam(1, 1,"", "name", "asc");
        actualResult = categoryMySQLGateway.findAll(query);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(movies.getId(), actualResult.items().get(0).getId());

        expectedPage = 2;
        query = new CategorySearchParam(2, 1,"", "name", "asc");
        actualResult = categoryMySQLGateway.findAll(query);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(series.getId(), actualResult.items().get(0).getId());
    }
    @Test
    public void givenPrePersistedCategoriesAndMostViewedCategoryAsTerms_whenCallsFindAllAndDescriptionMatchWithTerm_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies = Category.newCategory("Movies", "Most Viewed Category", true);
        final var series = Category.newCategory("Series", "Viewed Category", true);
        final var documentaries = Category.newCategory("Documentaries", "less Viewed Category", true);

        assertEquals(0, categoryRepository.count());
        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchParam(0, 1,"Most viewed", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(query);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(movies.getId(), actualResult.items().get(0).getId());

    }
    @Test
    public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndNameMatchWithTerm_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        assertEquals(0, categoryRepository.count());
        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchParam(0, 1,"Doc", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(query);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(documentaries.getId(), actualResult.items().get(0).getId());
    }
}
