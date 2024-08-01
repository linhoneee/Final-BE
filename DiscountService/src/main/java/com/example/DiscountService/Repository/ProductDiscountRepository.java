package com.example.DiscountService.Repository;

import com.example.DiscountService.Entity.ProductDiscount;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface ProductDiscountRepository extends R2dbcRepository<ProductDiscount, Long> {
    Flux<ProductDiscount> findByProductId(Long productId);
}
