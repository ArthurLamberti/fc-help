package com.arthurlamberti.admin.catalogo.application.category.create;

import com.arthurlamberti.admin.catalogo.application.UseCase;
import com.arthurlamberti.admin.catalogo.domain.validation.handler.NotificationHandler;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<NotificationHandler, CreateCategoryOutput>> {
}
