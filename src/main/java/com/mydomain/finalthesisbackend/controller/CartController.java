// cart api - 
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
        public ResponseEntity<CartDTO> addItemToCart(@RequestHeader("Authorization") String authHeader, @RequestBody ItemDTO itemDTO) {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // Extract userId from the JWT token
            String token = authHeader.substring(7);
            String userId = jwtUtil.extractUsername(token);

            // Convert ItemDTO to Item
            Item item = new Item();
            item.setId(itemDTO.getId());
            item.setName(itemDTO.getName());
            item.setPrice(itemDTO.getPrice());

            // Add item to the cart for the authenticated user
            CartDTO updatedCart = cartService.addToCart(userId, item);
            return ResponseEntity.ok(updatedCart);
        }

    @DeleteMapping("/remove-item")
    public ResponseEntity<CartDTO> removeItemFromCart(@RequestHeader("Authorization") String authHeader, @RequestBody ItemDTO itemDTO) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Extract userId from the JWT token
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUsername(token);

        // Use the item ID from ItemDTO
        String itemId = itemDTO.getId();

        // Remove item from the cart for the authenticated user
        CartDTO updatedCart = cartService.deleteFromCart(userId, itemId);
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint is accessible.");
    }

}