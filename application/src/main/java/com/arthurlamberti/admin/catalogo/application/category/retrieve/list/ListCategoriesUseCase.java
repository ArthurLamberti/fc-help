package com.arthurlamberti.admin.catalogo.application.category.retrieve.list;

import com.arthurlamberti.admin.catalogo.application.UseCase;
import com.arthurlamberti.admin.catalogo.domain.category.CategorySearchParam;
import com.arthurlamberti.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase
        extends UseCase<CategorySearchParam, Pagination<CategoryListOutput>> {
}
