package com.example.DiscountService.Controller;

import com.example.DiscountService.DTO.ApplyCouponRequest;
import com.example.DiscountService.DTO.DiscountResult;
import com.example.DiscountService.Entity.CustomerCoupon;
import com.example.DiscountService.Service.CustomerCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/customer-coupons")
public class CustomerCouponController {
    private final CustomerCouponService customerCouponService;

    @Autowired
    public CustomerCouponController(CustomerCouponService customerCouponService) {
        this.customerCouponService = customerCouponService;
    }

    @CrossOrigin
    @PostMapping
    public Mono<CustomerCoupon> createCustomerCoupon(@RequestBody CustomerCoupon customerCoupon) {
        return customerCouponService.createCustomerCoupon(customerCoupon);
    }

    @CrossOrigin
    @GetMapping
    public Flux<CustomerCoupon> getAllCustomerCoupons() {
        return customerCouponService.getAllCustomerCoupons();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public Mono<CustomerCoupon> getCustomerCouponById(@PathVariable Long id) {
        return customerCouponService.getCustomerCouponById(id);
    }

//    @CrossOrigin
//    @GetMapping("/{code}")
//    public Mono<CustomerCoupon> getCustomerCouponByCode(@PathVariable String code) {
//        return customerCouponService.getCustomerCouponByCode(code);
//    }

    @CrossOrigin
    @PostMapping("/apply")
    public Mono<DiscountResult> applyCoupon(@RequestBody ApplyCouponRequest request) {
        return customerCouponService.applyCoupon(request.getCode(), request.getOrderValue(), request.getShippingCost());
    }


    @CrossOrigin
    @PutMapping("/{id}")
    public Mono<CustomerCoupon> updateCustomerCoupon(@PathVariable Long id, @RequestBody CustomerCoupon customerCoupon) {
        return customerCouponService.updateCustomerCoupon(id, customerCoupon);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomerCoupon(@PathVariable Long id) {
        return customerCouponService.deleteCustomerCoupon(id);
    }
}



