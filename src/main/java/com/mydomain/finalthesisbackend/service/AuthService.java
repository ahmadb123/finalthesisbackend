/*
 * AuthService- 
 * Responsbile for functions and algorithms
 */
package com.mydomain.finalthesisbackend.service;
import com.mydomain.finalthesisbackend.model.User;
import com.mydomain.finalthesisbackend.repository.UserRepository;
import com.mydomain.finalthesisbackend.utility.JwtUtil;
import com.mydomain.finalthesisbackend.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.mydomain.finalthesisbackend.model.Address;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired 
    private UserRepository userRepository;


    public String authenticate(String username, String password) {
        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Verify password
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Authenticate user and generate JWT token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        
        return jwtUtil.generateToken(authentication.getName());
    }
    public void register(String username, String password, String emailAddress, String firstName, String lastName, String dateOfBirth, String phoneNumber, String gender, Address address) {
        if (userRepository.findByusername(username).isPresent()) {
            throw new RuntimeException("User already exists");
        }
    
        // Encrypt password and create user
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setusername(username);
        user.setPassword(encodedPassword);
        user.setEmailAddress(emailAddress);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress(address);
        user.setDateOfBirth(dateOfBirth != null ? dateOfBirth : null); // Explicitly set null if not provided
        user.setPhoneNumber(phoneNumber != null ? phoneNumber : null); // Explicitly set null if not provided
        user.setGender(gender != null ? gender : null); // Explicitly set null if not provided
    
        userRepository.save(user);
    }
    
    // retirve username from jwt token  
    public void updateAddress(String username, Address address) {
        User user = userRepository.findByusername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setAddress(address);
        userRepository.save(user);
    }

    public void deleteAddress(String username){
        User user = userRepository.findByusername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setAddress(null);
        userRepository.save(user);
    }
}
