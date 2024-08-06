package com.example.ReviewAndFeedbackService.Entity;

import com.example.ReviewAndFeedbackService.DTO.User;
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
@Table("review_responses")
public class ReviewResponse {

    @Id
    private Long id;

    @Column("review_id")
    private Long reviewId;

    @Column("user_id")
    private Long userId;

    @Column("response")
    private String response;

    @Column("created_at")
    private LocalDateTime createdAt;

}
