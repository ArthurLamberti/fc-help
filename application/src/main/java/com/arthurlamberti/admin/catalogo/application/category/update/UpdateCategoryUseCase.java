package com.arthurlamberti.admin.catalogo.application.category.update;

import com.arthurlamberti.admin.catalogo.application.UseCase;
import com.arthurlamberti.admin.catalogo.domain.validation.handler.NotificationHandler;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<NotificationHandler, UpdateCategoryOutPut>> {
}
