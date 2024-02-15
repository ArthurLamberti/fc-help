package com.arthurlamberti.admin.catalogo.domain.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append (Error anError);
    ValidationHandler append (ValidationHandler validationHandler);
    ValidationHandler validate (Validation validation);
    List<Error> getErrors();

    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default Error firstError() {
        if(getErrors() != null && !getErrors().isEmpty())
            return getErrors().get(0);
        return null;
    }

    interface Validation {
        void validate();
    }

}
