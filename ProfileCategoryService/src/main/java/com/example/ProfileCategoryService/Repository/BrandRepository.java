package com.example.ProfileCategoryService.Repository;

import com.example.ProfileCategoryService.Entity.Brand;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends ReactiveCrudRepository<Brand, Long> {
}
