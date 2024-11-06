package com.example.OrderService.Controller;

import com.example.OrderService.Entity.Order;
import com.example.OrderService.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Mono<Order> createOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }


    @CrossOrigin
    @GetMapping("/{userId}")
    public Flux<Order> getAllOrdersByUserId(@PathVariable int userId) {
        return orderService.getAllOrders(userId);
    }
    @PatchMapping("/{orderId}/products/{productId}/reviewed")
    public Mono<Order> markProductAsReviewed(@PathVariable Long orderId, @PathVariable int productId) {
        return orderService.markProductAsReviewed(orderId, productId);
    }


    @GetMapping("/revenue-by-hour")
    public Flux<Double> getRevenueByHour(@RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        return orderService.getRevenueByHour(date);
    }

    @GetMapping("/revenue-by-day-of-week")
    public Flux<Double> getRevenueByDayOfWeek(@RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        return orderService.getRevenueByDayOfWeek(date);
    }

    @GetMapping("/revenue-by-day-of-month")
    public Flux<Double> getRevenueByDayOfMonth(@RequestParam("year") int year, @RequestParam("month") int month) {
        return orderService.getRevenueByDayOfMonth(year, month);
    }

    @GetMapping("/revenue-by-month-of-year")
    public Flux<Double> getRevenueByMonthOfYear(@RequestParam("year") int year) {
        return orderService.getRevenueByMonthOfYear(year);
    }

    @GetMapping("/top-provinces")
    public Mono<Map<String, Long>>  getTopProvinces() {
        return orderService.getTop5Provinces();
    }
    // API để trả về tổng số tiền, tổng số đơn và top 10 sản phẩm bán chạy nhất
    @GetMapping("/api/order/statistics")
    public Mono<Map<String, Object>> getOrderStatistics() {
        return orderService.getOrderStatistics();
    }


    @GetMapping("/api/order/top-products")
    public Mono<List<Map<String, Object>>> getTopSellingProducts() {
        return orderService.getTopSellingProducts();
    }

    @PatchMapping("/{orderId}/update-location")
    public Mono<Order> updateOrderLocation(@PathVariable Long orderId, @RequestParam double currentLatitude, @RequestParam double currentLongitude) {
        return orderService.updateOrderLocation(orderId, currentLatitude, currentLongitude);
    }


}