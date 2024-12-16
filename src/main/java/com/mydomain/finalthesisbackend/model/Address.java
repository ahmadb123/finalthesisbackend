/*
 * Address model -
 */
package com.mydomain.finalthesisbackend.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Address {
    private String streetName;
    private String city;
    private String state;
    private String postalCode;
    private String country;


    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }


    // Setters

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
