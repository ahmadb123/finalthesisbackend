package com.mydomain.finalthesisbackend.service;

import com.mydomain.finalthesisbackend.dto.CartDTO;
import com.mydomain.finalthesisbackend.dto.CartItemDTO;
import com.mydomain.finalthesisbackend.model.Cart;
import com.mydomain.finalthesisbackend.model.Item;
import com.mydomain.finalthesisbackend.model.CartItem;
import com.mydomain.finalthesisbackend.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    // Retrieve cart by user ID and convert to CartDTO
    public CartDTO getCartByUserId(String userId) {
        Cart cart = cartRepository.findByUserId(userId);
        return convertToCartDTO(cart);
    }

    // Add item to cart and return CartDTO
    public CartDTO addToCart(String userId, Item item) {
        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setItems(new ArrayList<>());
        }

        List<CartItem> items = cart.getItems();
        boolean itemExists = false;

        // Check if the item already exists in the cart
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(item.getId())) {
                // If item already exists, increase quantity by 1
                items.get(i).setQuantity(items.get(i).getQuantity() + 1);
                itemExists = true;
                break;
            }
        }

        // If the item is new, add it to the cart
        if (!itemExists) {
            CartItem newItem = new CartItem(item.getId(), item.getName(), item.getPrice(), 1);
            items.add(newItem);
        }

        // Update cart totals and save
        updateCart(cart);
        cartRepository.save(cart);

        return convertToCartDTO(cart);
    }

    // Delete item from cart and return CartDTO
    public CartDTO deleteFromCart(String userId, String itemId) {
        Cart cart = cartRepository.findByUserId(userId);

        if (cart != null) {
            List<CartItem> items = cart.getItems();
            boolean itemRemoved = false;

            // Find the item by ID and remove it
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getId().equals(itemId)) {
                    CartItem item = items.get(i);
                    if(item.getQuantity() > 1){
                        // decrease quantity by 1
                        item.setQuantity(item.getQuantity() - 1);
                    } else {
                        items.remove(i);    
                    }
                    itemRemoved = true;
                    break;
                }
            }

            // Update and save cart if an item was removed
            if (itemRemoved) {
                updateCart(cart);
                cartRepository.save(cart);
            }
        }

        return convertToCartDTO(cart);
    }

    // Helper method to update count and total in the cart
    private void updateCart(Cart cart) {
        int count = 0;
        double total = 0;

        for (int i = 0; i < cart.getItems().size(); i++) {
            count += cart.getItems().get(i).getQuantity();
            total += cart.getItems().get(i).getPrice() * cart.getItems().get(i).getQuantity();
        }

        cart.setCount(count);
        cart.setTotal(total);
    }

    // Helper method to convert Cart to CartDTO
    private CartDTO convertToCartDTO(Cart cart) {
        if (cart == null) return null;

        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(cart.getUserId());
        cartDTO.setCount(cart.getCount());
        cartDTO.setTotal(cart.getTotal());

        // Convert each CartItem to CartItemDTO
        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(this::convertToCartItemDTO)
                .collect(Collectors.toList());
        cartDTO.setItems(itemDTOs);

        return cartDTO;
    }

    // Helper method to convert CartItem to CartItemDTO
    private CartItemDTO convertToCartItemDTO(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(cartItem.getId());
        dto.setName(cartItem.getName());
        dto.setPrice(cartItem.getPrice());
        dto.setQuantity(cartItem.getQuantity());
        return dto;
    }
}
