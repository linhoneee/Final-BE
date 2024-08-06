package com.example.ReviewAndFeedbackService.Controller;

import com.example.ReviewAndFeedbackService.DTO.ReviewRequest;
import com.example.ReviewAndFeedbackService.DTO.ReviewWithUser;
import com.example.ReviewAndFeedbackService.Entity.Review;
import com.example.ReviewAndFeedbackService.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @CrossOrigin
    @PostMapping
    public Mono<ResponseEntity<Review>> addReview(@RequestBody ReviewRequest reviewRequest) {
        return reviewService.addReview(reviewRequest.getProductId(), reviewRequest.getUserId(), reviewRequest.getRating(), reviewRequest.getComment(), reviewRequest.getParentId())
                .map(ResponseEntity::ok);
    }

    @CrossOrigin
    @GetMapping("/product/{productId}")
    public Flux<ReviewWithUser> getReviewsByProductId(@PathVariable Long productId) {
        return reviewService.getReviewsByProductId(productId);
    }

    @CrossOrigin
    @GetMapping("/{productId}")
    public Flux<Review> ReviewsByProductId(@PathVariable Long productId) {
        return reviewService.ReviewsByProductId(productId);
    }

    @CrossOrigin
    @GetMapping("/without-responses")
    public Flux<Review> getReviewsWithoutResponses() {
        return reviewService.getReviewsWithoutResponses();
    }
}
