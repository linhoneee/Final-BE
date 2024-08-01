package com.example.map_service.controller;

import com.example.map_service.Model.Address;
import com.example.map_service.entity.DistanceRecord;
import com.example.map_service.entity.Warehouse;
import com.example.map_service.service.DistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/distance")
public class DistanceController {

    @Autowired
    private DistanceService distanceService;

    @Autowired
    private RestTemplate restTemplate;

    @CrossOrigin
    @GetMapping("/calculate")
    public Mono<DistanceRecord> calculateNearestWarehouse(@RequestParam Integer userId, @RequestParam List<Long> warehouseIds) {
        return getPrimaryAddress(userId).flatMap(userAddress ->
                getWarehousesByIds(warehouseIds).collectList().flatMap(warehouses -> {
                    Warehouse nearestWarehouse = warehouses.stream()
                            .min(Comparator.comparingDouble(warehouse -> distanceService.calculateHaversineDistance(
                                    userAddress.getLatitude(), userAddress.getLongitude(),
                                    warehouse.getLatitude(), warehouse.getLongitude())))
                            .orElseThrow(() -> new RuntimeException("No warehouses found"));

                    DistanceRecord distanceRecord = distanceService.calculateShortestDistance(
                            userAddress, nearestWarehouse);

                    return Mono.just(distanceRecord);
                })
        );
    }

    @CrossOrigin
    @GetMapping("/calculateWithFullAddress")
    public Mono<DistanceRecord> calculateDistanceWithFullAddress(
            @RequestParam String receiverName,
            @RequestParam String street,
            @RequestParam String ward,
            @RequestParam String district,
            @RequestParam String provinceCity,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam List<Long> warehouseIds) {

        Address address = new Address();
        address.setReceiverName(receiverName);
        address.setStreet(street);
        address.setWard(ward);
        address.setDistrict(district);
        address.setProvinceCity(provinceCity);
        address.setLatitude(latitude);
        address.setLongitude(longitude);

        return getWarehousesByIds(warehouseIds).collectList().flatMap(warehouses -> {
            Warehouse nearestWarehouse = warehouses.stream()
                    .min(Comparator.comparingDouble(warehouse -> distanceService.calculateHaversineDistance(
                            latitude, longitude,
                            warehouse.getLatitude(), warehouse.getLongitude())))
                    .orElseThrow(() -> new RuntimeException("No warehouses found"));

            DistanceRecord distanceRecord = distanceService.calculateShortestDistance(
                    address, nearestWarehouse);

            return Mono.just(distanceRecord);
        });
    }

    private Mono<Address> getPrimaryAddress(Integer userId) {
        String url = "http://localhost:8080/addresses/primary/" + userId;
        return Mono.defer(() -> {
            ResponseEntity<Address> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Address.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return Mono.justOrEmpty(responseEntity.getBody());
            } else {
                return Mono.error(new RuntimeException("Failed to get primary address: " + responseEntity.getStatusCode()));
            }
        });
    }

    private Flux<Warehouse> getWarehousesByIds(List<Long> warehouseIds) {
        String warehouseIdsParam = String.join(",", warehouseIds.stream().map(String::valueOf).toArray(String[]::new));
        String url = "http://localhost:6007/api/warehouses?warehouseIds=" + warehouseIdsParam;
        return Flux.defer(() -> {
            ResponseEntity<Warehouse[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Warehouse[].class);
            Warehouse[] warehouses = responseEntity.getBody();
            if (responseEntity.getStatusCode().is2xxSuccessful() && warehouses != null) {
                return Flux.fromArray(warehouses);
            } else {
                return Flux.error(new RuntimeException("Failed to get warehouses: " + responseEntity.getStatusCode()));
            }
        });
    }
}
