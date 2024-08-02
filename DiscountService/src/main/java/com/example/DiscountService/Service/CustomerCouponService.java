package com.example.DiscountService.Service;

import com.example.DiscountService.DTO.DiscountResult;
import com.example.DiscountService.Entity.CustomerCoupon;
import com.example.DiscountService.Repository.CustomerCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class CustomerCouponService {
    private final CustomerCouponRepository customerCouponRepository;

    @Autowired
    public CustomerCouponService(CustomerCouponRepository customerCouponRepository) {
        this.customerCouponRepository = customerCouponRepository;
    }

    public Mono<DiscountResult> applyCoupon(String code, Double orderValue, Double shippingCost) {
        return customerCouponRepository.findByCode(code)
                .flatMap(coupon -> {
                    if (coupon.getEndDate() != null && coupon.getEndDate().isBefore(LocalDateTime.now())) {
                        return Mono.error(new RuntimeException("Coupon is expired"));
                    }
                    if (coupon.getCurrentUsage() >= coupon.getMaxUsage()) {
                        return Mono.error(new RuntimeException("Coupon usage limit exceeded"));
                    }
                    if (orderValue < coupon.getMinimumOrderValue()) {
                        return Mono.error(new RuntimeException("Order value is below the minimum required for this coupon"));
                    }

                    Double discountAmount;
                    Double discountedOrderValue = orderValue;
                    Double discountedShippingCost = shippingCost;

                    if ("SHIPPING".equalsIgnoreCase(coupon.getDiscountType())) {
                        discountAmount = shippingCost * (coupon.getDiscountPercentage() / 100);
                        discountedShippingCost -= discountAmount;
                    } else {
                        discountAmount = orderValue * (coupon.getDiscountPercentage() / 100);
                        discountedOrderValue -= discountAmount;
                    }


                    coupon.setCurrentUsage(coupon.getCurrentUsage() + 1);

                    return customerCouponRepository.save(coupon)
                            .thenReturn(new DiscountResult(discountAmount, coupon.getDiscountType(), discountedOrderValue, discountedShippingCost));
                });
    }

    public Mono<CustomerCoupon> createCustomerCoupon(CustomerCoupon customerCoupon) {
        return customerCouponRepository.save(customerCoupon);
    }

//    public Mono<CustomerCoupon> getCustomerCouponByCode(String code) {
//        return customerCouponRepository.findByCode(code);
//    }

    public Flux<CustomerCoupon> getAllCustomerCoupons() {
        return customerCouponRepository.findAll();
    }
    public Mono<CustomerCoupon> getCustomerCouponById(Long id) {
        return customerCouponRepository.findById(id);
    }

    public Mono<CustomerCoupon> updateCustomerCoupon(Long id, CustomerCoupon customerCoupon) {
        return customerCouponRepository.findById(id)
                .flatMap(existingCoupon -> {
                    existingCoupon.setCode(customerCoupon.getCode());
                    existingCoupon.setDescription(customerCoupon.getDescription());
                    existingCoupon.setDiscountPercentage(customerCoupon.getDiscountPercentage());
                    existingCoupon.setStartDate(customerCoupon.getStartDate());
                    existingCoupon.setEndDate(customerCoupon.getEndDate());
                    existingCoupon.setMaxUsage(customerCoupon.getMaxUsage());
                    existingCoupon.setMinimumOrderValue(customerCoupon.getMinimumOrderValue());
                    existingCoupon.setDiscountType(customerCoupon.getDiscountType());
                    return customerCouponRepository.save(existingCoupon);
                });
    }


    public Mono<Void> deleteCustomerCoupon(Long id) {
        return customerCouponRepository.deleteById(id);
    }
}
