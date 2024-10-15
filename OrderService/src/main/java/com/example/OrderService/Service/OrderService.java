package com.example.OrderService.Service;


import com.example.OrderService.Entity.Order;
import com.example.OrderService.Repository.OrderRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.linhmai.CommonService.model.DistanceDataOrderService;
import com.linhmai.CommonService.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
@Slf4j
@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public Mono<Order> saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Flux<Order> getAllOrders(int userId) {
        return orderRepository.findByUserId(userId);
    }

    public Mono<Order> markProductAsReviewed(Long orderId, int productId) {
        return orderRepository.findById(orderId)
                .flatMap(order -> {
                    try {
                        JsonNode itemsNode = objectMapper.readTree(order.getItems());
                        for (Iterator<JsonNode> it = itemsNode.elements(); it.hasNext(); ) {
                            JsonNode itemNode = it.next();
                            if (itemNode.get("productId").asInt() == productId) {
                                ((ObjectNode) itemNode).put("isReviewed", true);
                                break;
                            }
                        }
                        order.setItems(objectMapper.writeValueAsString(itemsNode));
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error updating review status", e));
                    }
                    return orderRepository.save(order);
                });
    }

    // Tính doanh thu theo giờ trong ngày
    public Flux<Double> getRevenueByHour(LocalDate day) {
        LocalDateTime startOfDay = day.atStartOfDay();
        return Flux.range(0, 24)
                .flatMap(hour -> {
                    LocalDateTime startOfHour = startOfDay.plusHours(hour);
                    LocalDateTime endOfHour = startOfHour.plusHours(1).minusSeconds(1);
                    return orderRepository.findAllByCreatedAtBetween(startOfHour, endOfHour)
                            .map(Order::getTotal)
                            .reduce(0.0, Double::sum);
                });
    }


    // Tính doanh thu theo từng ngày trong tuần
    public Flux<Double> getRevenueByDayOfWeek(LocalDate date) {
        LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return Flux.range(0, 7)
                .flatMap(dayOffset -> {
                    LocalDate day = startOfWeek.plusDays(dayOffset);
                    LocalDateTime startOfDay = day.atStartOfDay();
                    LocalDateTime endOfDay = day.atTime(23, 59, 59);
                    return orderRepository.findAllByCreatedAtBetween(startOfDay, endOfDay)
                            .map(Order::getTotal)
                            .reduce(0.0, Double::sum);
                });
    }

    // Tính doanh thu theo từng ngày trong tháng
    public Flux<Double> getRevenueByDayOfMonth(int year, int month) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        int daysInMonth = startOfMonth.toLocalDate().lengthOfMonth();  // Số ngày trong tháng
        return Flux.range(1, daysInMonth)
                .flatMap(day -> {
                    LocalDateTime startOfDay = startOfMonth.plusDays(day - 1).toLocalDate().atStartOfDay();
                    LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
                    return orderRepository.findAllByCreatedAtBetween(startOfDay, endOfDay)
                            .map(Order::getTotal)
                            .reduce(0.0, Double::sum);
                });
    }


    // Tính doanh thu theo từng tháng trong năm
    public Flux<Double> getRevenueByMonthOfYear(int year) {
        return Flux.range(1, 12)
                .flatMap(month -> {
                    LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
                    LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);
                    return orderRepository.findAllByCreatedAtBetween(startOfMonth, endOfMonth)
                            .map(Order::getTotal)
                            .reduce(0.0, Double::sum);
                });
    }

    // Trích xuất dữ liệu và tính toán top 5 tỉnh có nhiều đơn hàng nhất
    public Mono<Map<String, Long>> getTop5Provinces() {
        return orderRepository.findAll() // Lấy tất cả các đơn hàng
                .flatMap(order -> {
                    try {
                        // Trích xuất thông tin từ distanceData
                        DistanceDataOrderService distanceData = objectMapper.readValue(order.getDistanceData(), DistanceDataOrderService.class);
                        logger.info("Province City: " + distanceData.getProvinceCity()); // Log tên tỉnh
                        return Mono.just(distanceData.getProvinceCity()); // Trả về tên tỉnh thành
                    } catch (Exception e) {
                        logger.error("Error parsing distanceData for order: " + order.getId(), e); // Log chi tiết lỗi
                        return Mono.empty(); // Nếu có lỗi khi parse JSON, bỏ qua đơn hàng đó
                    }
                })
                .groupBy(province -> province) // Nhóm các đơn hàng theo tên tỉnh
                .flatMap(group -> group.count().map(count -> Map.entry(group.key(), count))) // Đếm số lần xuất hiện mỗi tỉnh
                .collectList()
                .map(entries -> entries.stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // Sắp xếp theo số lượng đơn hàng giảm dần
                        .limit(5) // Lấy top 5 tỉnh
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new // Đảm bảo thứ tự sắp xếp
                        ))
                );
    }

    public Mono<Map<String, Object>> getOrderStatistics() {
        return orderRepository.findAll()
                .collectList()
                .map(orders -> {
                    Map<String, Object> statistics = new HashMap<>();
                    double totalSales = 0;
                    int totalOrders = orders.size(); // Tổng số đơn hàng dựa trên số lượng bản ghi
                    Map<Long, Integer> productQuantities = new HashMap<>(); // Bản đồ để đếm số lượng bán được của mỗi sản phẩm

                    for (Order order : orders) {
                        // Cộng dồn giá trị "total" của mỗi đơn hàng vào tổng tiền
                        totalSales += order.getTotal();

                        String itemsJson = order.getItems();
                        try {
                            // Parse chuỗi JSON items thành List<Item>
                            List<Item> items = objectMapper.readValue(itemsJson, new TypeReference<List<Item>>() {});

                            // Tính tổng số lượng sản phẩm bán được
                            for (Item item : items) {
                                productQuantities.put(item.getProductId(), productQuantities.getOrDefault(item.getProductId(), 0) + item.getQuantity());
                            }
                        } catch (Exception e) {
                            e.printStackTrace(); // Log lỗi nếu có ngoại lệ
                        }
                    }

                    // Tính top 10 sản phẩm bán chạy nhất
                    List<Map.Entry<Long, Integer>> topProducts = productQuantities.entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .limit(10)
                            .collect(Collectors.toList());

                    // Chuẩn bị dữ liệu trả về
                    List<Map<String, Object>> topProductList = topProducts.stream()
                            .map(entry -> {
                                Map<String, Object> productData = new HashMap<>();
                                productData.put("productId", entry.getKey());
                                productData.put("quantitySold", entry.getValue());
                                return productData;
                            })
                            .collect(Collectors.toList());

                    // Đưa các kết quả vào đối tượng trả về
                    statistics.put("totalSales", totalSales);
                    statistics.put("totalOrders", totalOrders);
                    statistics.put("topProducts", topProductList);

                    return statistics;
                });
    }
}