package com.example.OrderService.Service;


import com.example.OrderService.Entity.Order;
import com.example.OrderService.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Mono<Order> saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Flux<Order> getAllOrders(int userId) {
        return orderRepository.findByUserId(userId);
    }
}