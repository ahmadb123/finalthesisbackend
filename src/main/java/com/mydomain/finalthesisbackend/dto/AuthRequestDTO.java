/*
 * Data transfer object class - 
 * This class serves as a Data Transfer Object (DTO) for authentication and registration requests.
 * Purpose: To transfer data between the client and the server in a structured and consistent manner.
 * This DTO is primarily used in the `AuthController` for endpoints like login and registration.
 */

package com.mydomain.finalthesisbackend.dto;

public class AuthRequestDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String dateOfBirth;
    private String gender;
    private String phoneNumber;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
}