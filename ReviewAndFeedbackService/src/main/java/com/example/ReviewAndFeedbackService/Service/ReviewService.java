package com.example.ReviewAndFeedbackService.Service;

import com.example.ReviewAndFeedbackService.DTO.User;
import com.example.ReviewAndFeedbackService.DTO.ReviewWithUser;
import com.example.ReviewAndFeedbackService.Entity.Review;
import com.example.ReviewAndFeedbackService.Repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Mono<Review> addReview(Long productId, Long userId, int rating, String comment, Long parentId) {
        if (parentId != null) {
            // Đây là phản hồi, không kiểm tra điều kiện
            Review review = new Review();
            review.setProductId(productId);
            review.setUserId(userId);
            review.setRating(rating);
            review.setComment(comment);
            review.setCreatedAt(LocalDateTime.now());
            review.setParentId(parentId);
            return reviewRepository.save(review);
        } else {
            // Đây là review mới, kiểm tra điều kiện
            return reviewRepository.existsByProductIdAndUserIdAndParentIdIsNull(productId, userId)
                    .flatMap(exists -> {
                        if (exists) {
                            return Mono.error(new IllegalArgumentException("User has already reviewed this product"));
                        }

                        Review review = new Review();
                        review.setProductId(productId);
                        review.setUserId(userId);
                        review.setRating(rating);
                        review.setComment(comment);
                        review.setCreatedAt(LocalDateTime.now());
                        review.setParentId(null);
                        return reviewRepository.save(review);
                    });
        }
    }

    public Flux<ReviewWithUser> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId)
                .flatMap(review -> getUserInfo(review.getUserId())
                        .map(user -> new ReviewWithUser(review, user)));
    }

    public Flux<Review> ReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public Flux<Review> getReviewsWithoutResponses() {
        return reviewRepository.findByParentIdIsNull()
                .filterWhen(review -> reviewRepository.findByParentId(review.getId())
                        .collectList()
                        .map(List::isEmpty));
    }

    private Mono<User> getUserInfo(Long userId) {
        String url = "http://localhost:8080/users/" + userId;
        return Mono.defer(() -> {
            ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, User.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return Mono.justOrEmpty(responseEntity.getBody());
            } else {
                return Mono.error(new RuntimeException("Failed to get user info: " + responseEntity.getStatusCode()));
            }
        });
    }
}
