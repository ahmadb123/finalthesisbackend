package com.mydomain.finalthesisbackend.controller;
import com.mydomain.finalthesisbackend.dto.AuthRequestDTO;
import com.mydomain.finalthesisbackend.dto.AuthResponseDTO;
import com.mydomain.finalthesisbackend.model.Address;
import com.mydomain.finalthesisbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.mydomain.finalthesisbackend.utility.JwtUtil;
import com.mydomain.finalthesisbackend.model.User;
import com.mydomain.finalthesisbackend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController 
@CrossOrigin(origins = "*") // Allow all origins, or specify your frontend's origin
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

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
    public ResponseEntity<?> updateAddress (@RequestHeader("Authorization") String authHeader,  @RequestBody Address address) {
        try {
            // check authorization - 
            if(authHeader == null || !authHeader.startsWith("Bearer")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing or invalid Authorization header");
                
            }
            // extract jwt token header - 
            String token = authHeader.substring(7);
            // extract username - 
            String username = jwtUtil.extractUsername(token);
            authService.updateAddress(username,address); 
            return ResponseEntity.ok("Address updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
    
    @GetMapping("/get-address")
    public ResponseEntity<Object> getAddress(@RequestHeader("Authorization") String authHeader) {
        try {
            // Check authorization
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing or invalid Authorization header");
            }
    
            // Extract JWT token from header
            String token = authHeader.substring(7);
    
            // Extract username from token
            String username = jwtUtil.extractUsername(token);
    
            // Retrieve address using the username
            User user = userRepository.findByusername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Address address = user.getAddress();
    
            // Return the address in the response
            return ResponseEntity.ok(address);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
    /*
     * Delete address by ID 
     * TODO: 
     */
    // @DeleteMapping("/delete-address-id")
    //     public ResponseEntity <String> deleteAddressId(@RequestHeader("Authorization") String authHeader, @RequestParam String id){
    //         try{

    //         }

    @DeleteMapping("/delete-address")
        public ResponseEntity <String> deleteAddress(@RequestHeader("Authorization") String authHeader){
        try{
                // check authorization - 
            if(authHeader == null || !authHeader.startsWith("Bearer")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing or invalid Authorization header");
            }
                // extract jwt token header - 
            String token = authHeader.substring(7);
                // extract username - 
            String username = jwtUtil.extractUsername(token);
                // delete address - 
            authService.deleteAddress(username);
            return ResponseEntity.ok("Address deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint is accessible.");
    }
        
}
