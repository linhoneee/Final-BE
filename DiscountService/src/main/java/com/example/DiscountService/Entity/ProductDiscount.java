package com.example.DiscountService.Entity;

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
@Table("product_discounts")
public class ProductDiscount {
    @Id
    private Long id;

    @Column("product_id")
    private Long productId;

    @Column("new_price")
    private Double newPrice;

    @Column("start_date")
    private LocalDateTime startDate;

    @Column("end_date")
    private LocalDateTime endDate; // Có thể null nếu không chọn thời gian kết thúc
}
