package com.mydomain.finalthesisbackend.controller;

import com.mydomain.finalthesisbackend.dto.AuthRequestDTO;
import com.mydomain.finalthesisbackend.dto.AuthResponseDTO;
import com.mydomain.finalthesisbackend.model.Address;
import com.mydomain.finalthesisbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

@RestController 
@CrossOrigin(origins = "*") // Allow all origins, or specify your frontend's origin
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequest){
        String token = authService.authenticate(authRequest.getUsername(), authRequest.getPassword());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequestDTO authRequest) {
        try {
            Address address = new Address();
            address.setStreetName(null);
            address.setCity(null);
            address.setState(null);
            address.setPostalCode(null);
            address.setCountry(null);
            
            authService.register(authRequest.getUsername(), authRequest.getPassword(), authRequest.getEmailAddress(), authRequest.getFirstName(), authRequest.getLastName(), address);
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/update-address")
    public ResponseEntity<?> updateAddress(@RequestParam String username, @RequestBody Address newAddress) {
        try {
            authService.updateAddress(username, newAddress);
            return ResponseEntity.ok("Address updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint is accessible.");
    }
        
}
