package com.arthurlamberti.admin.catalogo.domain.category;

import com.arthurlamberti.admin.catalogo.domain.validation.Error;
import com.arthurlamberti.admin.catalogo.domain.validation.ValidationHandler;
import com.arthurlamberti.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private final Category category;
    private final Integer MAX_LENGTH = 255;
    private final Integer MIN_LENGTH = 3;

    protected CategoryValidator(final Category category, final ValidationHandler handler) {
        super(handler);
        this.category = category;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.category.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if(name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final var length = name.trim().length();
        if(length > MAX_LENGTH || length < MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }
}
