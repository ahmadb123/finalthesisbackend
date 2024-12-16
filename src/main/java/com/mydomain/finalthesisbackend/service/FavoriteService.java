/*
 * Favorite Service 
 * Responsible for adding/deleting/loading functions for favortie data
 */
package com.mydomain.finalthesisbackend.service;

import com.mydomain.finalthesisbackend.model.Favorite;
import com.mydomain.finalthesisbackend.dto.FavoriteDTO;
import com.mydomain.finalthesisbackend.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    // retrieve favorites by user Id and convert to FavoriteDTO
    public FavoriteDTO getFavoritesByUserId(String userId){
        Favorite favorite = favoriteRepository.findByUserId(userId);
        return convertToFavoriteDTO(favorite);
    }

    // add favorite to user's favoirites  

    public FavoriteDTO addFavorites(String userId, String itemId){
        Favorite favorite = favoriteRepository.findByUserId(userId);
        if(favorite == null){
            favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setItemIds(new ArrayList<>());
        }
        if(!favorite.getItemIds().contains(itemId)){
            favorite.getItemIds().add(itemId);
            favoriteRepository.save(favorite);
        }
        updateFavorite(favorite);
        favoriteRepository.save(favorite);  
        return convertToFavoriteDTO(favorite);
    }

    // delete favorite from user's favorites
    public FavoriteDTO deleteFavorites(String userId, String itemId){
        Favorite favorite = favoriteRepository.findByUserId(userId);
        if(favorite != null){
            favorite.getItemIds().remove(itemId);
            favoriteRepository.save(favorite);
        }
        updateFavorite(favorite);
        favoriteRepository.save(favorite);
        return convertToFavoriteDTO(favorite);
    }

    // Helper method to convert Favorite to FavoriteDTO
    private FavoriteDTO convertToFavoriteDTO(Favorite favorite) {
        FavoriteDTO dto = new FavoriteDTO();
        dto.setId(favorite.getId());
        dto.setUserId(favorite.getUserId());
        dto.setItemIds(favorite.getItemIds());
        return dto;
    }

    // Helper method to update Favorite
    private void updateFavorite(Favorite favorite) {
        favoriteRepository.save(favorite);
    }

}
