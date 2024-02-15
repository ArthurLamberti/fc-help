package com.arthurlamberti.admin.catalogo.application.category.retrieve.get;

import com.arthurlamberti.admin.catalogo.domain.category.CategoryGateway;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryId;
import com.arthurlamberti.admin.catalogo.domain.exceptions.DomainException;
import com.arthurlamberti.admin.catalogo.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutPut execute(String anIn) {
        CategoryId categoryId = CategoryId.from(anIn);
        return this.categoryGateway.findById(categoryId)
                .map(CategoryOutPut::from)
                .orElseThrow(notFound(categoryId));
    }
    private static Supplier<DomainException> notFound(CategoryId anId) {
        return () -> DomainException.with(new Error("Category with id %s was not found".formatted(anId.getValue())));
    }
}
