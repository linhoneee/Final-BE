//package com.example.CartService.Service;
//
//import com.example.CartService.Entity.Item;
//import com.example.CartService.Repository.CartRepository;
//import com.example.cart_service.Entity.Cart;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//public class CartService {
//
//    private final CartRepository cartRepository;
//    private final ObjectMapper objectMapper;
//
//    public CartService(CartRepository cartRepository, ObjectMapper objectMapper) {
//        this.cartRepository = cartRepository;
//        this.objectMapper = objectMapper;
//    }
//
//    public Flux<Cart> findAll() {
//        return cartRepository.findAll();
//    }
//
//    public Mono<Cart> findByUserId(Long userId) {
//        return cartRepository.findByUserId(userId);
//    }
//
//    public Mono<Cart> addItemToCart(Long userId, Item item) {
//        return cartRepository.findByUserId(userId)
//                .flatMap(cart -> {
//                    List<Item> items = parseItems(cart.getItems());
//                    boolean itemExists = false;
//
//                    for (Item existingItem : items) {
//                        if (existingItem.getProductId().equals(item.getProductId())) {
//                            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
//                            itemExists = true;
//                            break;
//                        }
//                    }
//
//                    if (!itemExists) {
//                        items.add(item);
//                    }
//
//                    String updatedItemsJson = convertItemsToJson(items);
//                    cart.setItems(updatedItemsJson);
//                    cart.setTotal(calculateTotal(items));
//                    return cartRepository.save(cart);
//                })
//                .switchIfEmpty(createNewCart(userId, item));
//    }
//
//    public Mono<Cart> removeItemFromCart(Long userId, Item item) {
//        return cartRepository.findByUserId(userId)
//                .flatMap(cart -> {
//                    List<Item> items = parseItems(cart.getItems());
//                    log.info("Current items in cart: {}", items);
//
//                    items = items.stream()
//                            .map(existingItem -> {
//                                if (existingItem.getProductId().equals(item.getProductId())) {
//                                    log.info("Found product with ID: {}", existingItem.getProductId());
//                                    existingItem.setQuantity(existingItem.getQuantity() - item.getQuantity());
//                                }
//                                return existingItem;
//                            })
//                            .filter(existingItem -> existingItem.getQuantity() > 0)
//                            .collect(Collectors.toList());
//
//                    log.info("Updated items in cart after removal: {}", items);
//
//                    String updatedItemsJson = convertItemsToJson(items);
//                    cart.setItems(updatedItemsJson);
//                    cart.setTotal(calculateTotal(items));
//                    return cartRepository.save(cart);
//                })
//                .switchIfEmpty(Mono.error(new RuntimeException("Cart not found for user: " + userId)));
//    }
//
//    public Mono<Cart> clearItemFromCart(Long userId, Item item) {
//        return cartRepository.findByUserId(userId)
//                .flatMap(cart -> {
//                    List<Item> items = parseItems(cart.getItems());
//
//                    items = items.stream()
//                            .filter(existingItem -> !existingItem.getProductId().equals(item.getProductId()))
//                            .collect(Collectors.toList());
//
//                    String updatedItemsJson = convertItemsToJson(items);
//                    cart.setItems(updatedItemsJson);
//                    cart.setTotal(calculateTotal(items));
//                    return cartRepository.save(cart);
//                })
//                .switchIfEmpty(Mono.error(new RuntimeException("Cart not found for user: " + userId)));
//    }
//
//    private Mono<Cart> createNewCart(Long userId, Item item) {
//        List<Item> items = new ArrayList<>();
//        items.add(item);
//        Cart cart = new Cart();
//        cart.setUserId(userId);
//        cart.setItems(convertItemsToJson(items));
//        cart.setTotal(calculateTotal(items));
//        return cartRepository.save(cart);
//    }
//
//    private List<Item> parseItems(String itemsJson) {
//        try {
//            return objectMapper.readValue(itemsJson, new TypeReference<List<Item>>() {});
//        } catch (JsonProcessingException e) {
//            log.error("Error parsing items JSON", e);
//            return new ArrayList<>();
//        }
//    }
//
//    private String convertItemsToJson(List<Item> items) {
//        try {
//            return objectMapper.writeValueAsString(items);
//        } catch (JsonProcessingException e) {
//            log.error("Error converting items to JSON", e);
//            return "[]";
//        }
//    }
//
//    private BigDecimal calculateTotal(List<Item> items) {
//        return items.stream()
//                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
//}


package com.example.CartService.Service;

import com.example.CartService.Entity.Item;
import com.example.CartService.Repository.CartRepository;
import com.example.CartService.Entity.Cart;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ObjectMapper objectMapper;

    public CartService(CartRepository cartRepository, ObjectMapper objectMapper) {
        this.cartRepository = cartRepository;
        this.objectMapper = objectMapper;
    }

    public Flux<Cart> findAll() {
        return cartRepository.findAll();
    }

    public Mono<Cart> findByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public Mono<Cart> addItemToCart(Long userId, Item item) {
        return cartRepository.findByUserId(userId)
                .flatMap(cart -> {
                    List<Item> items = parseItems(cart.getItems());
                    boolean itemExists = false;

                    for (Item existingItem : items) {
                        if (existingItem.getProductId().equals(item.getProductId())) {
                            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                            existingItem.setWarehouseIds(item.getWarehouseIds()); // Update warehouseIds
                            existingItem.setPrimaryImageUrl(item.getPrimaryImageUrl()); // Update primaryImageUrl
                            itemExists = true;
                            break;
                        }
                    }

                    if (!itemExists) {
                        items.add(item);
                    }

                    String updatedItemsJson = convertItemsToJson(items);
                    cart.setItems(updatedItemsJson);
                    cart.setTotal(calculateTotal(items));
                    return cartRepository.save(cart);
                })
                .switchIfEmpty(createNewCart(userId, item));
    }

    public Mono<Cart> removeItemFromCart(Long userId, Item item) {
        return cartRepository.findByUserId(userId)
                .flatMap(cart -> {
                    List<Item> items = parseItems(cart.getItems());
                    log.info("Current items in cart: {}", items);

                    items = items.stream()
                            .map(existingItem -> {
                                if (existingItem.getProductId().equals(item.getProductId())) {
                                    log.info("Found product with ID: {}", existingItem.getProductId());
                                    existingItem.setQuantity(existingItem.getQuantity() - item.getQuantity());
                                }
                                return existingItem;
                            })
                            .filter(existingItem -> existingItem.getQuantity() > 0)
                            .collect(Collectors.toList());

                    log.info("Updated items in cart after removal: {}", items);

                    String updatedItemsJson = convertItemsToJson(items);
                    cart.setItems(updatedItemsJson);
                    cart.setTotal(calculateTotal(items));
                    return cartRepository.save(cart);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Cart not found for user: " + userId)));
    }

    public Mono<Cart> clearItemFromCart(Long userId, Item item) {
        return cartRepository.findByUserId(userId)
                .flatMap(cart -> {
                    List<Item> items = parseItems(cart.getItems());

                    items = items.stream()
                            .filter(existingItem -> !existingItem.getProductId().equals(item.getProductId()))
                            .collect(Collectors.toList());

                    String updatedItemsJson = convertItemsToJson(items);
                    cart.setItems(updatedItemsJson);
                    cart.setTotal(calculateTotal(items));
                    return cartRepository.save(cart);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Cart not found for user: " + userId)));
    }

    private Mono<Cart> createNewCart(Long userId, Item item) {
        List<Item> items = new ArrayList<>();
        items.add(item);
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setItems(convertItemsToJson(items));
        cart.setTotal(calculateTotal(items));
        return cartRepository.save(cart);
    }

    private List<Item> parseItems(String itemsJson) {
        try {
            return objectMapper.readValue(itemsJson, new TypeReference<List<Item>>() {});
        } catch (JsonProcessingException e) {
            log.error("Error parsing items JSON", e);
            return new ArrayList<>();
        }
    }

    private String convertItemsToJson(List<Item> items) {
        try {
            return objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            log.error("Error converting items to JSON", e);
            return "[]";
        }
    }

    private BigDecimal calculateTotal(List<Item> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
