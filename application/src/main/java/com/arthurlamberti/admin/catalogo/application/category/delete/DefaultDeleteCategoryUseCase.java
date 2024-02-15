package com.arthurlamberti.admin.catalogo.application.category.delete;

import com.arthurlamberti.admin.catalogo.domain.category.CategoryGateway;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryId;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }


    @Override
    public void execute(final String anIn) {
        this.categoryGateway.deleteById(CategoryId.from(anIn));
    }
}
