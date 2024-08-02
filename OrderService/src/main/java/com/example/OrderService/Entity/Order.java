package com.example.OrderService.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("orders")
public class Order {

    @Id
    private Long id;

    @Column("user_id")
    private int userId;

    @Column("items")
    private String items;

    @Column("selected_shipping")
    private String selectedShipping;

    @Column("distance_data")
    private String distanceData;

    @Column("total")
    private double total;

    @Column("created_at")
    private LocalDateTime createdAt;

    public Order(int userId, String items, String selectedShipping, String distanceData, double total) {
        this.userId = userId;
        this.items = items;
        this.selectedShipping = selectedShipping;
        this.distanceData = distanceData;
        this.total = total;
        this.createdAt = LocalDateTime.now();
    }
}
