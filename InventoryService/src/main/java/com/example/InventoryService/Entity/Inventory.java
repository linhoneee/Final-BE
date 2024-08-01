package com.example.InventoryService.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Table("inventory")
public class Inventory {


    @Id
    private Long id;

    @Column("product_id")
    private int productId;

    @Column("product_name")
    private String productName;

    @Column("quantity")
    private int quantity;

    @Column("description")
    private String description;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    public Inventory() {
        // Cập nhật thời gian khi tạo mới
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
}