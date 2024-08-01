package com.example.ProfileCategoryService.Controller;

import com.example.ProfileCategoryService.Entity.Category;
import com.example.ProfileCategoryService.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @CrossOrigin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Category> createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public Mono<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }
    @CrossOrigin
    @GetMapping
    public Flux<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public Mono<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }
}

