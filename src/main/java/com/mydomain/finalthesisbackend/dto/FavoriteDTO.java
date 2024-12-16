/*
 * Favorite Data transfer data
 * Responsible for transferring data from backend/database to frontend
 */
package com.mydomain.finalthesisbackend.dto;

import java.util.List;

public class FavoriteDTO {
    private String id;
    private String userId;
    private List<String> itemIds;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<String> itemIds) {
        this.itemIds = itemIds;
    }
}
