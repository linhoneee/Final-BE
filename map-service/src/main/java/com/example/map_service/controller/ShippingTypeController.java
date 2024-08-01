package com.example.map_service.controller;

import com.example.map_service.entity.ShippingType;
import com.example.map_service.service.ShippingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/shipping-types")
public class ShippingTypeController {

    @Autowired
    private ShippingTypeService service;

    @CrossOrigin
    @GetMapping
    public Flux<ShippingType> getAllShippingTypes() {
        return service.getAllShippingTypes();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public Mono<ShippingType> getShippingTypeById(@PathVariable Long id) {
        return service.getShippingTypeById(id);
    }

    @CrossOrigin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ShippingType> createShippingType(@RequestBody ShippingType shippingType) {
        return service.createShippingType(shippingType);
    }

    @CrossOrigin
    @PutMapping("/{id}")
    public Mono<ShippingType> updateShippingType(@PathVariable Long id, @RequestBody ShippingType shippingType) {
        return service.updateShippingType(id, shippingType);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteShippingType(@PathVariable Long id) {
        return service.deleteShippingType(id);
    }
}
