/*
 * CartItem model 
 * construction - id, name, price, quanitity, image
 */
package com.mydomain.finalthesisbackend.model;

public class CartItem {
    private String id;
    private String name;
    private double price;
    private int quantity;
    private String image;
    // Constructor
    public CartItem(String id, String name, double price, int quantity , String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

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
