package com.example.ReviewAndFeedbackService.Entity;

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
@Table("reviews")
public class Review {

    @Id
    private Long id;

    @Column("product_id")
    private Long productId;

    @Column("user_id")
    private Long userId;

    @Column("rating")
    private Integer rating;

    @Column("comment")
    private String comment;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("parent_id")
    private Long parentId;
}
