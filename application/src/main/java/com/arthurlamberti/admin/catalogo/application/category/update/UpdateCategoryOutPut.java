package com.arthurlamberti.admin.catalogo.application.category.update;

import com.arthurlamberti.admin.catalogo.domain.category.Category;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryId;

public record UpdateCategoryOutPut(
        String id
) {
    public static UpdateCategoryOutPut from(
            final Category aCategory
    ) {
        return new UpdateCategoryOutPut(aCategory.getId().getValue());
    }

    public static UpdateCategoryOutPut from(
            final String anId
    ) {
        return new UpdateCategoryOutPut(anId);
    }


}
