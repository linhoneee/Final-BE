package com.example.ProfileCategoryService.Repository;

import com.example.ProfileCategoryService.Entity.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
    @Query("SELECT p.*, pi.* FROM products p LEFT JOIN product_images pi ON p.id = pi.product_id WHERE p.id = :id")
    Flux<Product> findProductWithImages(Long id);

    @Query("SELECT p.*, pi.* FROM products p LEFT JOIN product_images pi ON p.id = pi.product_id")
    Flux<Product> findAllProductsWithImages();
}

