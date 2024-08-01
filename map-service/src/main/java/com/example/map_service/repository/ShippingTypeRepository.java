package com.example.map_service.repository;

import com.example.map_service.entity.ShippingType;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingTypeRepository extends ReactiveCrudRepository<ShippingType, Long> {
}
