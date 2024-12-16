/*
 *Favorite controller allows customers (who are logged in) to save items
 * In the favorite page frontend 
*/

package com.mydomain.finalthesisbackend.controller;
import com.mydomain.finalthesisbackend.dto.FavoriteDTO;
import com.mydomain.finalthesisbackend.model.Favorite;
import com.mydomain.finalthesisbackend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mydomain.finalthesisbackend.utility.JwtUtil;
import java.util.Map;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/add-favorites")
    public ResponseEntity<FavoriteDTO> addFavorites(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> payload) {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        
        // extract userId 
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUsername(token);
        String itemId = payload.get("id");
        if (itemId == null) {
            return ResponseEntity.badRequest().build();
        }

        // add item to user's favorites
        FavoriteDTO favoriteDTO = favoriteService.addFavorites(userId, itemId);
        return ResponseEntity.ok(favoriteDTO);
    }

    @GetMapping("/get-favorites")
    public ResponseEntity<?> getFavoritesByUserId(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    
        // Extract userId
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUsername(token);
    
        // Get user's favorites
        List<String> favoriteItemIds = favoriteService.getFavoritesByUserId(userId).getItemIds();
    
        // Return the list of item IDs
        return ResponseEntity.ok(favoriteItemIds);
    }
    
}
