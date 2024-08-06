package com.example.ReviewAndFeedbackService.Repository;

import com.example.ReviewAndFeedbackService.Entity.Review;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReviewRepository extends ReactiveCrudRepository<Review, Long> {
    Flux<Review> findByProductId(Long productId);
    Mono<Boolean> existsByProductIdAndUserIdAndParentIdIsNull(Long productId, Long userId);
    Flux<Review> findByParentIdIsNull();
    Flux<Review> findByParentId(Long parentId);
}
