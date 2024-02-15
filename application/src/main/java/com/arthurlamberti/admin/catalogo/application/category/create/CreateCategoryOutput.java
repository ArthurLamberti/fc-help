package com.arthurlamberti.admin.catalogo.application.category.create;

import com.arthurlamberti.admin.catalogo.domain.category.Category;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryId;

public record CreateCategoryOutput(
        String id
) {
    public static CreateCategoryOutput from(final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }

    public static CreateCategoryOutput from(final String anId) {
        return new CreateCategoryOutput(anId);
    }
}
