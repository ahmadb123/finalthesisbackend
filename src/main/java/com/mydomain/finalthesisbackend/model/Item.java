// Item class contains all the information about the item that will be added to the cart.

package com.mydomain.finalthesisbackend.model;

public class Item{
    private String id;
    private String name;
    private String description;
    private double price;

    public void setId(String id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setPrice(double price){
        this.price = price;
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public double getPrice(){
        return price;
    }
    
}