package com.example.ProfileCategoryService.Repository;

import com.example.ProfileCategoryService.Entity.Category;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {
}
