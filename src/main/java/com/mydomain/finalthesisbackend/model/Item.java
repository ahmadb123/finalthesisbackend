// Item class contains all the information about the item that will be added to the cart.

package com.mydomain.finalthesisbackend.model;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "items")

public class Item{
    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    private String image;
    private static final int MAX_STOCK = 50; // max stock per item
    private int stockQuantity; // current stock quantity
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

    public void setImage(String image){
        this.image = image;
    }

    public void setStockQuantity(int stockQuantity){
        if(stockQuantity <= MAX_STOCK){
            this.stockQuantity = stockQuantity;
        } else {
            this.stockQuantity = MAX_STOCK;
        }
    }

    public String getImage(){
        return image;
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

    public int getStockQuantity(){
        return stockQuantity;
    }

}