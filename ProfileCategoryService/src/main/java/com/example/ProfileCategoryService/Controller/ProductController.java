package com.example.ProfileCategoryService.Controller;
import com.example.ProfileCategoryService.Entity.Product;
import com.example.ProfileCategoryService.Entity.ProductImage;
import com.example.ProfileCategoryService.Model.ProductDTO;
import com.example.ProfileCategoryService.Model.ProductDTOuser;
import com.example.ProfileCategoryService.Service.ProductImageService;
import com.example.ProfileCategoryService.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;

    @CrossOrigin
    @PostMapping
    public Mono<Product> createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @CrossOrigin
    @GetMapping
    public Flux<Product> getAllProducts() {
        return productService.getAllProducts();
    }
    @CrossOrigin
    @GetMapping("/user")
    public Flux<ProductDTOuser> getAllProductsByUser() {
        return productService.getAllProductsByUser();
    }


    @CrossOrigin
    @GetMapping("/images")
    public Flux<ProductImage> getAllProductImages() {
        return productImageService.getAllProductImages();
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public Mono<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    @CrossOrigin
    @PutMapping("/{id}")
    public Mono<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }



    @CrossOrigin
    @DeleteMapping("/images/{id}")  // Add this endpoint
    public Mono<Void> deleteProductImage(@PathVariable Long id) {
        return productImageService.deleteProductImage(id);
    }

    @CrossOrigin
    @PostMapping("/{id}/images")
    public Mono<List<ProductImage>> uploadProductImages(@PathVariable Long id, @RequestPart("images") Flux<FilePart> files) {
        return productService.uploadProductImages(id, files);
    }

    @PutMapping("/{productId}/images/{imageId}/primary")
    public Mono<ResponseEntity<ProductImage>> setPrimaryImage(@PathVariable Long productId, @PathVariable Long imageId) {
        return productService.setPrimaryImage(productId, imageId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}



