package com.example.map_service.service;

import com.example.map_service.entity.ShippingType;
import com.example.map_service.repository.ShippingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ShippingTypeService {

    @Autowired
    private ShippingTypeRepository repository;

    public Flux<ShippingType> getAllShippingTypes() {
        return repository.findAll();
    }

    public Mono<ShippingType> getShippingTypeById(Long id) {
        return repository.findById(id);
    }

    public Mono<ShippingType> createShippingType(ShippingType shippingType) {
        return repository.save(shippingType);
    }

    public Mono<ShippingType> updateShippingType(Long id, ShippingType shippingType) {
        return repository.findById(id)
                .flatMap(existingType -> {
                    existingType.setName(shippingType.getName());
                    existingType.setPricePerKm(shippingType.getPricePerKm());
                    existingType.setPricePerKg(shippingType.getPricePerKg());
                    return repository.save(existingType);
                });
    }

    public Mono<Void> deleteShippingType(Long id) {
        return repository.deleteById(id);
    }
}
