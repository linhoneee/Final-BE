package com.example.ReviewAndFeedbackService.DTO;

import com.example.ReviewAndFeedbackService.Entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewWithUser {
    private Review review;
    private User user;
}
