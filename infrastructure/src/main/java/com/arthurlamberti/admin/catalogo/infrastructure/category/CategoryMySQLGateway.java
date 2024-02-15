package com.arthurlamberti.admin.catalogo.infrastructure.category;

import com.arthurlamberti.admin.catalogo.domain.category.Category;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryGateway;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryId;
import com.arthurlamberti.admin.catalogo.domain.category.CategorySearchParam;
import com.arthurlamberti.admin.catalogo.domain.pagination.Pagination;
import com.arthurlamberti.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.arthurlamberti.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.arthurlamberti.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;

import static com.arthurlamberti.admin.catalogo.infrastructure.utils.SpecificationUtils.like;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    public CategoryMySQLGateway(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category aCategory) {
        return this.categoryRepository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }

    @Override
    public void deleteById(final CategoryId anId) {
        final String anIdValue = anId.getValue();
        if(this.categoryRepository.existsById(anIdValue)){
            this.categoryRepository.deleteById(anIdValue);
        }
    }

    @Override
    public Optional<Category> findById(final CategoryId anId) {
        return this.categoryRepository.findById(anId.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return this.categoryRepository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }

    @Override
    public Pagination<Category> findAll(CategorySearchParam aQuery) {

        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var specification = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(str ->
                     SpecificationUtils.<CategoryJpaEntity>like("name", str)
                             .or(like("description", str))
                )
                .orElse(null);

        final var result =this.categoryRepository.findAll(Specification.where(specification),page);
        return new Pagination<>(
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.map(CategoryJpaEntity::toAggregate).toList()
        );
    }
}
