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

    @Query("SELECT * FROM products WHERE " +
            "(:searchTerm IS NULL OR LOWER(product_name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
            "(:brandId IS NULL OR brand_id = :brandId) AND " +
            "(:categoryId IS NULL OR category_id = :categoryId) " +
            "ORDER BY CASE WHEN :sortOrder = 'asc' THEN price END ASC, " +
            "CASE WHEN :sortOrder = 'desc' THEN price END DESC")
    Flux<Product> findAllByFilters(String searchTerm, Long brandId, Long categoryId, String sortOrder);

}

