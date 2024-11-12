package com.mydomain.finalthesisbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "users") // MongoDB collection

public class User{
    @Id
    private String id;
    private String username;
    private String password;
    private String role; // customer/manager 
    private Address address;
    private String emailAddress;
    private String firstName;
    private String lastName;
    //getters
    public String getId(){
        return id;
    }

    public String getusername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getRole(){
        return role;
    }
    //setters:
    public void setId(String id){
        this.id = id;
    }
    
    public void setusername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getEmailAddress(){
        return emailAddress;
    }
    public Address getAddress(){
        return address;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }
    public void setAddress(Address address){
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}