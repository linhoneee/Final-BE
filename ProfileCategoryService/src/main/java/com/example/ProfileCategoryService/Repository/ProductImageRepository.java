package com.example.ProfileCategoryService.Repository;

import com.example.ProfileCategoryService.Entity.ProductImage;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductImageRepository extends ReactiveCrudRepository<ProductImage, Long> {
    Flux<ProductImage> findByProductId(Long productId);
    Flux<Void> deleteAllByProductId(Long productId);
    Mono<Void> deleteById(Long id);  // Add this line
    Flux<ProductImage> findAllByProductId(Long productId);
    Mono<ProductImage> findByProductIdAndIsPrimaryTrue(Long productId); // Add this method

}
