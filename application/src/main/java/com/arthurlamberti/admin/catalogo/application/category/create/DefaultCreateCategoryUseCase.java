package com.arthurlamberti.admin.catalogo.application.category.create;

import com.arthurlamberti.admin.catalogo.domain.category.Category;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryGateway;
import com.arthurlamberti.admin.catalogo.domain.validation.handler.NotificationHandler;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

//@Named
public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }


    @Override
    public Either<NotificationHandler, CreateCategoryOutput> execute(CreateCategoryCommand aCommand) {
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.isActive();
        final var notificationHandler = NotificationHandler.create();

        final var aCategory = Category.newCategory(aName, aDescription, isActive);
        aCategory.validate(notificationHandler);

        return notificationHandler.hasErrors() ? API.Left(notificationHandler) : create(aCategory);
    }

    private Either<NotificationHandler, CreateCategoryOutput> create(Category aCategory) {
        return API.Try(() -> this.categoryGateway.create(aCategory))
                .toEither()
                .bimap(NotificationHandler::create, CreateCategoryOutput::from);
    }
}
