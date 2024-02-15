package com.arthurlamberti.admin.catalogo.domain.validation.handler;

import com.arthurlamberti.admin.catalogo.domain.exceptions.DomainException;
import com.arthurlamberti.admin.catalogo.domain.validation.Error;
import com.arthurlamberti.admin.catalogo.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class NotificationHandler implements ValidationHandler {

    private final List<Error> errors;

    public NotificationHandler(List<Error> erros) {
        this.errors = erros;
    }

    public static NotificationHandler create() {
        return new NotificationHandler(new ArrayList<>());
    }

    public static NotificationHandler create(final Error error) {
        return new NotificationHandler(new ArrayList<>()).append(error);
    }

    public static NotificationHandler create(final Throwable t) {
        return create(new Error(t.getMessage()));
    }


    @Override
    public NotificationHandler append(final Error anError) {
        this.errors.add(anError);
        return this;
    }



    @Override
    public NotificationHandler append(ValidationHandler validationHandler) {
        this.errors.addAll(validationHandler.getErrors());
        return this;
    }

    @Override
    public NotificationHandler validate(Validation validation) {
        try{
            validate(validation);
        } catch (final DomainException de) {
            this.errors.addAll(de.getErrors());
        } catch (final Exception ex) {
            this.errors.add(new Error(ex.getMessage()));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }
}
