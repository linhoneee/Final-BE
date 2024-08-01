package com.example.DiscountService.Service;

import com.example.DiscountService.Entity.ProductDiscount;
import com.example.DiscountService.Repository.ProductDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class ProductDiscountService {
    private final ProductDiscountRepository productDiscountRepository;

    @Autowired
    public ProductDiscountService(ProductDiscountRepository productDiscountRepository) {
        this.productDiscountRepository = productDiscountRepository;
    }

    public Flux<ProductDiscount> findAll() {
        return productDiscountRepository.findAll();
    }
    public Mono<ProductDiscount> findById(Long id) {
        return productDiscountRepository.findById(id);
    }
    public Mono<ProductDiscount> createProductDiscount(ProductDiscount productDiscount) {
        return productDiscountRepository.save(productDiscount);
    }

    public Flux<ProductDiscount> getProductDiscounts(Long productId) {
        return productDiscountRepository.findByProductId(productId);
    }

    public Mono<Void> deleteProductDiscount(Long id) {
        return productDiscountRepository.deleteById(id);
    }
    public Mono<ProductDiscount> updateProductDiscount(Long id, ProductDiscount productDiscount) {
        return productDiscountRepository.findById(id)
                .flatMap(existingDiscount -> {
                    existingDiscount.setProductId(productDiscount.getProductId());
                    existingDiscount.setNewPrice(productDiscount.getNewPrice());
                    existingDiscount.setStartDate(productDiscount.getStartDate());
                    existingDiscount.setEndDate(productDiscount.getEndDate());
                    return productDiscountRepository.save(existingDiscount);
                });
    }

}
