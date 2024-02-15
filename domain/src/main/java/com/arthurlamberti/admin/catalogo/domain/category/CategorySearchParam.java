package com.arthurlamberti.admin.catalogo.domain.category;

public record CategorySearchParam(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}
