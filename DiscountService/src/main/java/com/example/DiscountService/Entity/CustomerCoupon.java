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
@Table("customer_coupons")
public class CustomerCoupon {
    @Id
    private Long id;

    @Column("code")
    private String code;

    @Column("description")
    private String description;

    @Column("discount_percentage")
    private Double discountPercentage;

    @Column("start_date")
    private LocalDateTime startDate;

    @Column("end_date")
    private LocalDateTime endDate; // Có thể null nếu không chọn thời gian kết thúc

    @Column("max_usage")
    private Double maxUsage;

    @Column("current_usage")
    private Integer currentUsage;

    @Column("minimum_order_value")
    private Double minimumOrderValue;

    @Column("discount_type")
    private String discountType; // SHIPPING hoặc PRODUCT
}
