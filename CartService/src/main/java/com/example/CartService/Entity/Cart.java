package com.example.CartService.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("carts")
public class Cart {
    @Id
    private Long id;
    @Column("user_id")
    private Long userId;
    private String items; // JSON representation of items
    private BigDecimal total;

}
