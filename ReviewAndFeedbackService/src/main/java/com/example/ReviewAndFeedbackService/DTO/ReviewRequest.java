package com.example.ReviewAndFeedbackService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
    private Long productId;
    private Long userId;
    private int rating;
    private String comment;
    private Long parentId;
}
