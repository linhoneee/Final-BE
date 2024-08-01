//package com.example.ProfileCategoryService.Controller;
//
//import com.example.ProfileCategoryService.Entity.ProductImage;
//import com.example.ProfileCategoryService.Service.ProductImageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//@CrossOrigin(origins = "http://localhost:3000")
//
//@RestController
//@RequestMapping("/product_images")
//public class ProductImageController {
//
//    @Autowired
//    private ProductImageService productImageService;
//
//    @CrossOrigin
//    @GetMapping
//    public Flux<ProductImage> getAllProductImages() {
//        return productImageService.getAllProductImages();
//    }
//
//    @CrossOrigin
//    @GetMapping("/{id}")
//    public Mono<ProductImage> getProductImageById(@PathVariable Long id) {
//        return productImageService.getProductImageById(id);
//    }
//
//    @CrossOrigin
//    @PostMapping
//    public Mono<ProductImage> createProductImage(@RequestBody ProductImage productImage) {
//        return productImageService.createProductImage(productImage);
//    }
//
//    @CrossOrigin
//    @PutMapping("/{id}")
//    public Mono<ProductImage> updateProductImage(@PathVariable Long id, @RequestBody ProductImage productImage) {
//        return productImageService.updateProductImage(id, productImage);
//    }
//
//    @CrossOrigin
//    @DeleteMapping("/{id}")
//    public Mono<Void> deleteProductImage(@PathVariable Long id) {
//        return productImageService.deleteProductImage(id);
//    }
//}
