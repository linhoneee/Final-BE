package com.example.ProfileCategoryService.Service;

import com.example.ProfileCategoryService.Entity.Brand;
import com.example.ProfileCategoryService.Repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;
    public Flux<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Mono<Brand> createBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public Mono<Brand> getBrandById(Long id) {
        return brandRepository.findById(id);
    }

    public Mono<Brand> updateBrand(Long id, Brand brand) {
        return brandRepository.findById(id)
                .flatMap(existingBrand -> {
                    existingBrand.setName(brand.getName());
                    existingBrand.setDescription(brand.getDescription());
                    return brandRepository.save(existingBrand);
                });
    }

    public Mono<Void> deleteBrand(Long id) {
        return brandRepository.deleteById(id);
    }
}

