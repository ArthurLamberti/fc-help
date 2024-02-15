package com.arthurlamberti.admin.catalogo.application.category.retrieve.list;

import com.arthurlamberti.admin.catalogo.domain.category.CategoryGateway;
import com.arthurlamberti.admin.catalogo.domain.category.CategorySearchParam;
import com.arthurlamberti.admin.catalogo.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoryUseCase extends ListCategoriesUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultListCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }


    @Override
    public Pagination<CategoryListOutput> execute(CategorySearchParam anIn) {
        return this.categoryGateway.findAll(anIn)
                .map(CategoryListOutput::from);
    }
}
