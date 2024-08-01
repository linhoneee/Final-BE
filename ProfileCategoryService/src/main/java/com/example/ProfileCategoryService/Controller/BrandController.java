package com.example.ProfileCategoryService.Controller;

import com.example.ProfileCategoryService.Entity.Brand;
import com.example.ProfileCategoryService.Service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:3000")

@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;
    @CrossOrigin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Brand> createBrand(@RequestBody Brand brand) {
        return brandService.createBrand(brand);
    }

    @CrossOrigin
    @GetMapping
    public Flux<Brand> getAllBrands() {
        return brandService.getAllBrands();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public Mono<Brand> getBrandById(@PathVariable Long id) {
        return brandService.getBrandById(id);
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public Mono<Brand> updateBrand(@PathVariable Long id, @RequestBody Brand brand) {
        return brandService.updateBrand(id, brand);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBrand(@PathVariable Long id) {
        return brandService.deleteBrand(id);
    }
}

