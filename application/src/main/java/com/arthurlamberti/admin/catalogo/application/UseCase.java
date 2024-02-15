package com.arthurlamberti.admin.catalogo.application;

import com.arthurlamberti.admin.catalogo.domain.category.Category;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIn);

}