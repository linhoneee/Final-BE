package com.example.OrderService.Repository;

import com.example.OrderService.Entity.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
    Flux<Order> findByUserId(int userId);
}