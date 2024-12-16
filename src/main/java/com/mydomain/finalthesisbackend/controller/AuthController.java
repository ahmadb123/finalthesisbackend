/* 
 * AuthController manages authentication/login api calls
 * Add/remove/checks database for creditinials
 */

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
import com.mydomain.finalthesisbackend.dto.UserDTO;

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

    // login controller
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequest){
        String token = authService.authenticate(authRequest.getUsername(), authRequest.getPassword());
        String userId = jwtUtil.extractUsername(token); // Extracts the username
        return ResponseEntity.ok(new AuthResponseDTO(token, "user", userId)); 
    }

    // register controller
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequestDTO authRequest) {
        try {
            // set address to null until changed
            Address address = new Address();
            address.setStreetName(null);
            address.setCity(null);
            address.setState(null);
            address.setPostalCode(null);
            address.setCountry(null);
            
            authService.register(authRequest.getUsername(), authRequest.getPassword(), authRequest.getEmailAddress(), authRequest.getFirstName(), authRequest.getLastName(), 
                                authRequest.getDateOfBirth(), authRequest.getGender(), authRequest.getPhoneNumber() , address);
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    // update-address should be in address controller folder.. will be updated
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
    
    // get current address 
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

    // get user details - first, last and email address -
    @GetMapping("/get-user-details")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            User user = userRepository.findByusername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Create a DTO from the user entity
            UserDTO userDetailsDTO = new UserDTO(
                    user.getusername(),
                    user.getPassword(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmailAddress(),
                    user.getPhoneNumber(),
                    user.getDateOfBirth(),
                    user.getGender()
            );

            return ResponseEntity.ok(userDetailsDTO);
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

    @PostMapping("/guest-login")
    public ResponseEntity<?> guestLogin() {
        String token = jwtUtil.generateGuestToken();
        return ResponseEntity.ok(new AuthResponseDTO(token, "guest"));
    }
    
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint is accessible.");
    }
        
}
