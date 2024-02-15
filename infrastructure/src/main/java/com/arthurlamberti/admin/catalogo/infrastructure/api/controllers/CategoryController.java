package com.arthurlamberti.admin.catalogo.infrastructure.api.controllers;

import com.arthurlamberti.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.arthurlamberti.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.arthurlamberti.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.arthurlamberti.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.arthurlamberti.admin.catalogo.domain.category.CategorySearchParam;
import com.arthurlamberti.admin.catalogo.domain.pagination.Pagination;
import com.arthurlamberti.admin.catalogo.domain.validation.handler.NotificationHandler;
import com.arthurlamberti.admin.catalogo.infrastructure.api.CategoryApi;
import com.arthurlamberti.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.arthurlamberti.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryApi {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final ListCategoriesUseCase listCategoriesUseCase
    ) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
    }


    @Override
    public ResponseEntity<?> createCategory(
            final CreateCategoryRequest input
    ) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );
        final Function<NotificationHandler, ResponseEntity<?>> onError = notificationHandler ->
                ResponseEntity.unprocessableEntity().body(notificationHandler);
        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = createCategoryOutput ->
                ResponseEntity.created(URI.create("/categories/".concat(createCategoryOutput.id()))).body(createCategoryOutput);

        return this.createCategoryUseCase.execute(aCommand).fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return listCategoriesUseCase.execute(new CategorySearchParam(page, perPage, search, sort, direction))
                .map(CategoryApiPresenter::present);
    }
}
