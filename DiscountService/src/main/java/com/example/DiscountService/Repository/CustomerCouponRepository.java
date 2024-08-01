package com.example.DiscountService.Repository;

import com.example.DiscountService.Entity.CustomerCoupon;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface CustomerCouponRepository extends R2dbcRepository<CustomerCoupon, Long> {
    Mono<CustomerCoupon> findByCode(String code);
}
