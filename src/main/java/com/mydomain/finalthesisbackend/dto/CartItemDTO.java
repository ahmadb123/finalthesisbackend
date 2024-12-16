/*
 * CartItemDTO
 * Serves as transfer data object
 * each cart is an array of items. 
 */

package com.mydomain.finalthesisbackend.dto;

public class CartItemDTO {
    private String id;
    private String name;
    private double price;
    private int quantity;
    private String image;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

}
