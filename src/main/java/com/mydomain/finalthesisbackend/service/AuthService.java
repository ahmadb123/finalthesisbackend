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
    public void register(String username, String password) {
        // Check if user already exists
        if (userRepository.findByusername(username).isPresent()) {
            throw new RuntimeException("User already exists");
        }
    
        // Encrypt password and create user
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setusername(username);
        user.setPassword(encodedPassword);
    
        // Save user to database
        userRepository.save(user);
    }
    
    
}
