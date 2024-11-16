// cart api - 
package com.mydomain.finalthesisbackend.controller;
import com.mydomain.finalthesisbackend.dto.CartDTO;
import com.mydomain.finalthesisbackend.dto.ItemDTO;
import com.mydomain.finalthesisbackend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")

public class CartController{
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCartByUserId(@RequestHeader("Authorization") String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUsername(token); // Extracts userId or username from JWT
        CartDTO cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<CartDTO> addItemToCart(@@RequestHeader("Authorization") String authHeader, @RequestBody ItemDTO itemDTO){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);  // Return a null body or a relevant error message if preferred
        }
    
        // Extract userId from the JWT token
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUsername(token); // Extracts userId or username from JWT
    
        // Add item to the cart for the authenticated user
        CartDTO updatedCart = cartService.addToCart(userId, itemDTO);
        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("/{userId}/remove")
    public ResponseEntity<CartDTO> removeItemFromCart(@RequestHeader("Authorization") String authHeader, @RequestBody ItemDTO itemDTO){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);  // Return a null body or a relevant error message if preferred
        }
    
        // Extract userId from the JWT token
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUsername(token); // Extracts userId or username from JWT
    
        // Remove item from the cart for the authenticated user
        CartDTO updatedCart = cartService.deleteFromCart(userId, itemDTO);
        return ResponseEntity.ok(updatedCart);
    }
}