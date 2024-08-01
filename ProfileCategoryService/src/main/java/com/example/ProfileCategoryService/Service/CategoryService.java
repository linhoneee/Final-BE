package com.example.ProfileCategoryService.Service;

import com.example.ProfileCategoryService.Entity.Category;
import com.example.ProfileCategoryService.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Mono<Category> createCategory(Category category) {
        return categoryRepository.save(category);
    }
    public Flux<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Mono<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Mono<Category> updateCategory(Long id, Category category) {
        return categoryRepository.findById(id)
                .flatMap(existingCategory -> {
                    existingCategory.setName(category.getName());
                    existingCategory.setDescription(category.getDescription());
                    return categoryRepository.save(existingCategory);
                });
    }

    public Mono<Void> deleteCategory(Long id) {
        return categoryRepository.deleteById(id);
    }
}
