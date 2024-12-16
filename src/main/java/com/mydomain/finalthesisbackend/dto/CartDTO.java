/*
 * CartDTO
 * This class serves as Data Transfer object
 * Transfers data for each cart found in the database. sends it to the frontend 
 */

package com.mydomain.finalthesisbackend.dto;

import java.util.List;

public class CartDTO {
    private String userId;
    private List<CartItemDTO> items;
    private int count;
    private double total;
    
    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
