// cart api - 

/*
 * Cart controller returns current cart, add item to cart, 
 * if customer is logged as guest and add item to cart, 
 * before checkout customer must sign in: merge controller, merges or adds
 * guest cart to user cart once logged in
 */
package com.mydomain.finalthesisbackend.controller;
import com.mydomain.finalthesisbackend.dto.CartDTO;
import com.mydomain.finalthesisbackend.dto.ItemDTO;
import com.mydomain.finalthesisbackend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.mydomain.finalthesisbackend.utility.JwtUtil;
import org.springframework.web.bind.annotation.*;
import com.mydomain.finalthesisbackend.model.Item;
import java.util.Map;
@RestController
@CrossOrigin(origins = "*") // Allow all origins, or specify your frontend's origin
@RequestMapping("/api/cart")
public class CartController{
    private final CartService cartService;

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/get-cart")
    public ResponseEntity<?> getCartByUserId(@RequestHeader("Authorization") String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUsername(token); // Extracts userId or username from JWT
        CartDTO cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add-item")
    public ResponseEntity<CartDTO> addItemToCart(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody Map<String, String> payload) {
    
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    
        // Extract userId from the JWT token
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUsername(token);
    
        String itemId = payload.get("id");
        if (itemId == null) {
            return ResponseEntity.badRequest().build();
        }
    
        // Create an Item object with only the ID
        Item item = new Item();
        item.setId(itemId);
    
        // Add item to the cart for the authenticated user
        CartDTO updatedCart = cartService.addToCart(userId, item);
        return ResponseEntity.ok(updatedCart);
    }
    
    // merge guest with logged in user cart
    @PostMapping("/merge-guest-cart")
    public ResponseEntity<CartDTO> mergeGuestCart(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CartDTO guestCart) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUsername(token);

        CartDTO updatedCart = cartService.mergeGuestWithUserCart(guestCart.getUserId(), userId);
        return ResponseEntity.ok(updatedCart);
    }

    // remove item from cart pass itemId
    @DeleteMapping("/remove-item/{itemId}")
        public ResponseEntity<CartDTO> removeItemFromCart(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String itemId) {
        
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        
            // Extract userId from the JWT token
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUsername(token);
        
            // Remove item from the cart for the authenticated user
            CartDTO updatedCart = cartService.deleteFromCart(userId, itemId);
            return ResponseEntity.ok(updatedCart);
        }
        
        

        @GetMapping("/test")
        public ResponseEntity<String> test() {
            return ResponseEntity.ok("Test endpoint is accessible.");
        }


}