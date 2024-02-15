package com.arthurlamberti.admin.catalogo.application.category.update;

import com.arthurlamberti.admin.catalogo.domain.category.Category;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryGateway;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryId;
import com.arthurlamberti.admin.catalogo.domain.exceptions.DomainException;
import com.arthurlamberti.admin.catalogo.domain.validation.Error;
import com.arthurlamberti.admin.catalogo.domain.validation.handler.NotificationHandler;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<NotificationHandler, UpdateCategoryOutPut> execute(UpdateCategoryCommand aCommand) {
        final var anId = CategoryId.from(aCommand.id());
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.isActive();

        final var aCategory = this.categoryGateway.findById(anId).orElseThrow(notFound(anId));
        final var notification = NotificationHandler.create();

        aCategory.update(aName, aDescription, isActive).validate(notification);
        return notification.hasErrors() ? Left(notification) : update(aCategory);
    }

    private Either<NotificationHandler, UpdateCategoryOutPut> update(Category aCategory) {
        return API.Try(() -> this.categoryGateway.update(aCategory))
                .toEither()
                .bimap(NotificationHandler::create, UpdateCategoryOutPut::from);
    }

    private static Supplier<DomainException> notFound(CategoryId anId) {
        return () -> DomainException.with(new Error("Category with id %s was not found".formatted(anId.getValue())));
    }
}
