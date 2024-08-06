package com.example.OrderService.Service;


import com.example.OrderService.Entity.Order;
import com.example.OrderService.Repository.OrderRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Iterator;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public Mono<Order> saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Flux<Order> getAllOrders(int userId) {
        return orderRepository.findByUserId(userId);
    }

    public Mono<Order> markProductAsReviewed(Long orderId, int productId) {
        return orderRepository.findById(orderId)
                .flatMap(order -> {
                    try {
                        JsonNode itemsNode = objectMapper.readTree(order.getItems());
                        for (Iterator<JsonNode> it = itemsNode.elements(); it.hasNext(); ) {
                            JsonNode itemNode = it.next();
                            if (itemNode.get("productId").asInt() == productId) {
                                ((ObjectNode) itemNode).put("isReviewed", true);
                                break;
                            }
                        }
                        order.setItems(objectMapper.writeValueAsString(itemsNode));
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error updating review status", e));
                    }
                    return orderRepository.save(order);
                });
    }
}