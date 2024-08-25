package com.example.CartService.Controller;

import com.example.CartService.Service.CartService;
import com.example.CartService.Entity.Item;
import com.example.CartService.Entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/hi")
    public String getHi() {
        return "hihi";
    }


    @CrossOrigin
    @GetMapping("/{userId}")
    public Mono<Cart> getCart(@PathVariable Long userId) {
        return cartService.findByUserId(userId);
    }

    @CrossOrigin
    @PostMapping("/{userId}")
    public Mono<Cart> addItemToCart(@PathVariable Long userId, @RequestBody Item item) {
        return cartService.addItemToCart(userId, item);
    }

    @CrossOrigin
    @PutMapping("/{userId}")
    public Mono<Cart> removeItemFromCart(@PathVariable Long userId, @RequestBody Item item) {
        return cartService.removeItemFromCart(userId, item);
    }

    @CrossOrigin
    @DeleteMapping("/{userId}")
    public Mono<Cart> clearItemFromCart(@PathVariable Long userId, @RequestBody Item item) {
        return cartService.clearItemFromCart(userId, item);
    }
}
