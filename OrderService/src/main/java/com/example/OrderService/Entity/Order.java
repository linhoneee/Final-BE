package com.example.OrderService.Entity;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Table("orders")
public class Order {

    @Id
    private Long id;

    @Column("user_id")
    private int userId;

    @Column("items")
    private String itemsJson; // Lưu dưới dạng JSON

    @Column("total")
    private double total;

    @Column("created_at")
    private LocalDateTime createdAt;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Order() {
        this.createdAt = LocalDateTime.now();
    }

    public Order(int userId, List<CartItem> items, double total) throws JsonProcessingException {
        this.userId = userId;
        this.setItems(items);
        this.total = total;
        this.createdAt = LocalDateTime.now();
    }

    // Getters và setters

    public List<CartItem> getItems() throws IOException {
        return objectMapper.readValue(this.itemsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, CartItem.class));
    }

    public void setItems(List<CartItem> items) throws JsonProcessingException {
        this.itemsJson = objectMapper.writeValueAsString(items);
    }
}