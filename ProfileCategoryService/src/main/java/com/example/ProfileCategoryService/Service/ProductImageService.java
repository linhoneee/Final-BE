package com.example.ProfileCategoryService.Service;

import com.example.ProfileCategoryService.Entity.ProductImage;
import com.example.ProfileCategoryService.Repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    public Flux<ProductImage> getAllProductImages() {
        return productImageRepository.findAll();
    }

    public Flux<ProductImage> getImagesByProductId(Long productId) {
        return productImageRepository.findByProductId(productId);
    }

    public Mono<Void> deleteProductImage(Long id) {  // Add this method
        return productImageRepository.deleteById(id);
    }

}
