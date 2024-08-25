package com.example.ProfileCategoryService.Service;

import com.example.ProfileCategoryService.Entity.Product;
import com.example.ProfileCategoryService.Entity.ProductImage;
import com.example.ProfileCategoryService.Model.ProductDTO;
import com.example.ProfileCategoryService.Model.ProductDTOuser;
import com.example.ProfileCategoryService.Repository.ProductImageRepository;
import com.example.ProfileCategoryService.Repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductImageService productImageService;

    public Mono<Product> saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Flux<ProductDTOuser> getAllProductsByUser() {
        return productRepository.findAll()
                .flatMap(product -> productImageRepository.findByProductIdAndIsPrimaryTrue(product.getId())
                        .map(image -> new ProductDTOuser(product, image))
                        .defaultIfEmpty(new ProductDTOuser(product, null)));
    }

    public Mono<Void> deleteProduct(Long id) {
        return productImageRepository.deleteAllByProductId(id)
                .then(productRepository.deleteById(id));
    }

    public Mono<ProductDTO> getProductById(Long id) {
        Mono<Product> productMono = productRepository.findById(id);
        Flux<ProductImage> imagesFlux = productImageService.getImagesByProductId(id);

        return productMono.zipWith(imagesFlux.collectList(), (product, images) -> new ProductDTO(product, images));
    }

    public Mono<Product> updateProduct(Long id, Product product) {
        return productRepository.findById(id)
                .flatMap(existingProduct -> {
                    existingProduct.setProductName(product.getProductName());
                    existingProduct.setDescriptionDetails(product.getDescriptionDetails());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setWeight(product.getWeight());
                    return productRepository.save(existingProduct);
                });
    }

    public Mono<List<ProductImage>> uploadProductImages(Long productId, Flux<FilePart> files) {
        return files.flatMap(file -> saveFile(file)
                        .flatMap(url -> {
                            ProductImage productImage = new ProductImage(null, productId, url, false);
                            return productImageRepository.save(productImage)
                                    .doOnSuccess(savedImage -> log.info("Saved image to DB: " + savedImage))
                                    .doOnError(error -> log.error("Error saving image to DB: " + error.getMessage()));
                        })
                ).collectList()
                .doOnSuccess(images -> log.info("All images uploaded successfully for product ID: " + productId))
                .doOnError(error -> log.error("Error uploading images for product ID: " + productId + ": " + error.getMessage()));
    }

    private Mono<String> saveFile(FilePart file) {
        Path basePath = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            if (!Files.exists(basePath)) {
                Files.createDirectories(basePath);
            }
            log.info("Base path for uploads: " + basePath.toString());
        } catch (IOException e) {
            log.error("Error creating base directory for uploads", e);
            return Mono.error(e);
        }

        Path path = basePath.resolve(UUID.randomUUID().toString() + "-" + file.filename());

        return file.transferTo(path)
                .then(Mono.just("/uploads/" + path.getFileName().toString()))
                .doOnSuccess(url -> log.info("File saved: " + url))
                .doOnError(error -> log.error("Error saving file: " + error.getMessage()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    // Method to set a primary image
    public Mono<ProductImage> setPrimaryImage(Long productId, Long imageId) {
        return productImageRepository.findAllByProductId(productId)
                .flatMap(image -> {
                    if (image.getId().equals(imageId)) {
                        image.setIsPrimary(true);
                    } else {
                        image.setIsPrimary(false);
                    }
                    return productImageRepository.save(image);
                })
                .then(productImageRepository.findById(imageId)); // Return the primary image
    }

    public Flux<Product> getProductsByFilters(String searchTerm, Long brandId, Long categoryId, String sortOrder) {
        return productRepository.findAllByFilters(searchTerm, brandId, categoryId, sortOrder);
    }
}
