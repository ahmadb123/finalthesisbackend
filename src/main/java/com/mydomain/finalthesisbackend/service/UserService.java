package com.mydomain.finalthesisbackend.service;
import com.mydomain.finalthesisbackend.model.Address;
import com.mydomain.finalthesisbackend.model.User;
import com.mydomain.finalthesisbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password, String role, String emailAddress, String firstName, String lastName, String dateOfBirth, String gender, String phoneNumber , Address address) {
        User user = new User();
        user.setusername(username);
        user.setPassword(passwordEncoder.encode(password)); // Hash the password
        user.setRole(role);
        user.setEmailAddress(emailAddress);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress(address);
        user.setDateOfBirth("");
        user.setPhoneNumber("");
        user.setGender("");
        return userRepository.save(user);
    }
}
