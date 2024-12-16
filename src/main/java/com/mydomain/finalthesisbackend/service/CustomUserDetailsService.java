/*
 * CustomUserDetailsService:
 * Responsible for handling loadingusername, setting user role
 */

package com.mydomain.finalthesisbackend.service;
import com.mydomain.finalthesisbackend.model.User;
import com.mydomain.finalthesisbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByusername(username) // Adjust to match method name case
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Set default role if it's missing
        String role = user.getRole();
        if (role == null) {
            role = "ROLE_USER"; // Set to "ROLE_USER" by convention
            user.setRole(role); // Persist if necessary
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getusername()) // Adjust to match getter name
                .password(user.getPassword()) // hashed password
                .roles(role.replace("ROLE_", "")) // remove "ROLE_" prefix when adding role
                .build();
    }
}
