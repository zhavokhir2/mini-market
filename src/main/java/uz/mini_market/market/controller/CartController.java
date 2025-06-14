package uz.mini_market.market.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.mini_market.market.dto.AddCartItemRequest;
import uz.mini_market.market.exception.ResourceNotFoundException;
import uz.mini_market.market.model.Cart;
import uz.mini_market.market.service.CartService;
import uz.mini_market.market.service.UserService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<Cart> getCart() {
        Long userId = userService.getCurrentUserId();
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addItemToCart(@RequestBody AddCartItemRequest request) {
        Long userId = userService.getCurrentUserId();
        Cart updatedCart = cartService.addItemToCart(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long cartItemId) {
        Long userId = userService.getCurrentUserId();
        Cart updatedCart = cartService.removeItemFromCart(userId, cartItemId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Cart> clearCart() {
        Long userId = userService.getCurrentUserId();
        Cart clearedCart = cartService.clearCart(userId);
        return ResponseEntity.ok(clearedCart);
    }
}


