//package com.example.map_service.controller;
//
//import com.example.map_service.entity.Warehouse;
//import com.example.map_service.repository.WarehouseRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/warehouse")
//public class WarehouseController {
//
//    @Autowired
//    private WarehouseRepository warehouseRepository;
//
//    @PostMapping("/create")
//    public Warehouse createWarehouse(@RequestParam String name, @RequestParam String address,
//                                     @RequestParam double latitude, @RequestParam double longitude) {
//        Warehouse warehouse = new Warehouse();
//        warehouse.setName(name);
//        warehouse.setAddress(address);
//        warehouse.setLatitude(latitude);
//        warehouse.setLongitude(longitude);
//        return warehouseRepository.save(warehouse);
//    }
//}
