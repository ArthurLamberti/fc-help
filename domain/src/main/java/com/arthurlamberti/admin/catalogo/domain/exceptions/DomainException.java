package com.arthurlamberti.admin.catalogo.domain.exceptions;

import com.arthurlamberti.admin.catalogo.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStackTraceException {

    private final List<Error> errors;

    private DomainException(final String aMessage, List<Error> errors) {
        super(aMessage);
        this.errors = errors;
    }

    public static DomainException with(final Error anError) {
        return new DomainException("",List.of(anError));
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException("",anErrors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
