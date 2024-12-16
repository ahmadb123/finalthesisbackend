/*
 * Cart model 
 */

package com.mydomain.finalthesisbackend.model;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// cart class - 

@Document(collection = "carts")
public class Cart {
    @Id
    private String id;
    private String userId; // username
    private List<CartItem> items; // list of items
    private int count; // items in cart
    private double total; // total price of items in cart

    // getters and setters: 

    public void setId(String id){
        this.id = id;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    public void setItems(List<CartItem> items){
        this.items = items;
    }
    public void setCount(int count){
        this.count = count;
    }
    public void setTotal(double total){
        this.total = total;
    }

    public String getId(){
        return id;
    }
    public String getUserId(){
        return userId;
    }
    public List<CartItem> getItems(){
        return items;
    }
    public int getCount(){
        return count;
    }
    public double getTotal(){
        return total;
    }
}


