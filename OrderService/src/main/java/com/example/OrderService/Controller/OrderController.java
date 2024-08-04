package com.example.OrderService.Controller;

import com.example.OrderService.Entity.Order;
import com.example.OrderService.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Mono<Order> createOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }


    @CrossOrigin
    @GetMapping("/{userId}")
    public Flux<Order> getAllOrdersByUserId(@PathVariable int userId) {
        return orderService.getAllOrders(userId);
    }
}