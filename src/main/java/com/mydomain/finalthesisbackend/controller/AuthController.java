package com.mydomain.finalthesisbackend.controller;

import com.mydomain.finalthesisbackend.dto.AuthRequestDTO;
import com.mydomain.finalthesisbackend.dto.AuthResponseDTO;
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
            authService.register(authRequest.getUsername(), authRequest.getPassword());
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint is accessible.");
    }
        
}
