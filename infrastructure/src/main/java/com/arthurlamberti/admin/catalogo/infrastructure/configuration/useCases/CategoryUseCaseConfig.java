package com.arthurlamberti.admin.catalogo.infrastructure.configuration.useCases;

import com.arthurlamberti.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.arthurlamberti.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import com.arthurlamberti.admin.catalogo.application.category.delete.DefaultDeleteCategoryUseCase;
import com.arthurlamberti.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.arthurlamberti.admin.catalogo.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.arthurlamberti.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.arthurlamberti.admin.catalogo.application.category.retrieve.list.DefaultListCategoryUseCase;
import com.arthurlamberti.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.arthurlamberti.admin.catalogo.application.category.update.DefaultUpdateCategoryUseCase;
import com.arthurlamberti.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.arthurlamberti.admin.catalogo.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoryUseCase() {
        return new DefaultListCategoryUseCase(categoryGateway);
    }
    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

}
