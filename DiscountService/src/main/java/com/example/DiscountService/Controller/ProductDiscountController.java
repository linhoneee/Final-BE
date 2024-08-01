package com.example.DiscountService.Controller;

import com.example.DiscountService.Entity.ProductDiscount;
import com.example.DiscountService.Service.ProductDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/product-discounts")
public class ProductDiscountController {
    private final ProductDiscountService productDiscountService;

    @Autowired
    public ProductDiscountController(ProductDiscountService productDiscountService) {
        this.productDiscountService = productDiscountService;
    }
    @CrossOrigin
    @GetMapping
    public Flux<ProductDiscount> getAllProductDiscounts() {
        return productDiscountService.findAll();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public Mono<ProductDiscount> getProductDiscountById(@PathVariable Long id) {
        return productDiscountService.findById(id);
    }


    @CrossOrigin
    @PostMapping
    public Mono<ProductDiscount> createProductDiscount(@RequestBody ProductDiscount productDiscount) {
        return productDiscountService.createProductDiscount(productDiscount);
    }

    @CrossOrigin
    @GetMapping("/product/{productId}")
    public Flux<ProductDiscount> getProductDiscounts(@PathVariable Long productId) {
        return productDiscountService.getProductDiscounts(productId);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public Mono<Void> deleteProductDiscount(@PathVariable Long id) {
        return productDiscountService.deleteProductDiscount(id);
    }
    @CrossOrigin
    @PutMapping("/{id}")
    public Mono<ProductDiscount> updateProductDiscount(@PathVariable Long id, @RequestBody ProductDiscount productDiscount) {
        return productDiscountService.updateProductDiscount(id, productDiscount);
    }

}
