package com.arthurlamberti.admin.catalogo.application.category.retrieve.list;

import com.arthurlamberti.admin.catalogo.IntegrationTest;
import com.arthurlamberti.admin.catalogo.domain.category.Category;
import com.arthurlamberti.admin.catalogo.domain.category.CategorySearchParam;
import com.arthurlamberti.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.arthurlamberti.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
public class ListCateogoriesUseCaseIT {
    @Autowired
    private ListCategoriesUseCase listCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void mockUp() {

        categoryRepository.saveAllAndFlush(
                List.of(
                        Category.newCategory("Series", null, true),
                        Category.newCategory("Netflix Originals", "Movies made by netflix", true),
                        Category.newCategory("Amazon Originals", "Movies made by Amazon", true),
                        Category.newCategory("HBO Originals", "Movies made by HBO", true),
                        Category.newCategory("Sports", null, true)
                ).stream().map(CategoryJpaEntity::from).toList()
        );
    }
    @Test
    public void givenAValidTerm_whenTermDoesntMatchsPrePersisted_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ji1j3i 1j3i1oj";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery =
                new CategorySearchParam(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = listCategoryUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
    }
    @ParameterizedTest
    @CsvSource({
            "Ser,0,10,1,1,Series",
            "net,0,10,1,1,Netflix Originals",
            "ZON,0,10,1,1,Amazon Originals",
            "by Amazon,0,10,1,1,Amazon Originals",
    })
    public void givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new CategorySearchParam(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = listCategoryUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Amazon Originals",
            "name,desc,0,10,5,5,Sports",
            "createdAt,asc,0,10,5,5,Series",
            "createdAt,desc,0,10,5,5,Sports",
    })
    public void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedTerms = "";

        final var aQuery =
                new CategorySearchParam(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = listCategoryUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Amazon Originals;HBO Originals",
            "1,2,2,5,Netflix Originals;Series",
            "2,2,1,5,Sports",
    })
    public void givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoriesName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var aQuery =
                new CategorySearchParam(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = listCategoryUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for (final String expectedName : expectedCategoriesName.split(";")) {
            final String actualName = actualResult.items().get(index).name();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
    }

}
