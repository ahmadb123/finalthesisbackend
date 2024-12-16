/*
 * CartService service:
 * Responsible for handling functions/deletion/adding/updating cart 
 */
package com.mydomain.finalthesisbackend.service;

import com.mydomain.finalthesisbackend.dto.CartDTO;
import com.mydomain.finalthesisbackend.dto.CartItemDTO;
import com.mydomain.finalthesisbackend.model.Cart;
import com.mydomain.finalthesisbackend.model.Item;
import com.mydomain.finalthesisbackend.model.CartItem;
import com.mydomain.finalthesisbackend.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mydomain.finalthesisbackend.repository.ItemRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ItemRepository itemRepository) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
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
    
        // Fetch the full item from the database to get the image and other details
        Item fullItem = itemRepository.findById(item.getId()).orElse(null);
        if (fullItem == null) {
            // Handle the case where the item is not found
            throw new RuntimeException("Item not found with ID: " + item.getId());
        }else if(fullItem.getStockQuantity() < 1){
            throw new RuntimeException("Item out of stock");
        }
    
        // Check if the item already exists in the cart
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(item.getId())) {
                // If item already exists, increase quantity by 1
                if (items.get(i).getQuantity() < fullItem.getStockQuantity()) {
                    items.get(i).setQuantity(items.get(i).getQuantity() + 1);
                    // decrease item stock 
                    fullItem.setStockQuantity(fullItem.getStockQuantity() - 1);
                    itemRepository.save(fullItem);
                }
                itemExists = true;
                break;
            }
        }
        // If the item is new, add it to the cart
        if (!itemExists) {
            CartItem newItem = new CartItem(
                fullItem.getId(),
                fullItem.getName(),
                fullItem.getPrice(),
                1,
                fullItem.getImage()
            );
            items.add(newItem);
            fullItem.setStockQuantity(fullItem.getStockQuantity() - 1);
            itemRepository.save(fullItem);
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
    
            for (int i = 0; i < items.size(); i++) {
                CartItem cartItem = items.get(i);
    
                if (cartItem.getId().equals(itemId)) {
                    Item fullItem = itemRepository.findById(itemId).orElse(null);
                    if (fullItem == null) {
                        throw new RuntimeException("Item not found with ID: " + itemId);
                    }
    
                    // Reduce quantity or remove item
                    if (cartItem.getQuantity() > 1) {
                        cartItem.setQuantity(cartItem.getQuantity() - 1);
                    } else {
                        items.remove(i);
                    }
    
                    // Increment stock in the inventory
                    fullItem.setStockQuantity(fullItem.getStockQuantity() + 1);
                    itemRepository.save(fullItem);
    
                    itemRemoved = true;
                    break;
                }
            }
    
            if (itemRemoved) {
                updateCart(cart);
                cartRepository.save(cart);
            }
        } else {
            throw new RuntimeException("Cart not found for user ID: " + userId);
        }
    
        return convertToCartDTO(cart);
    }    

    public CartDTO mergeGuestWithUserCart(String guestId, String userId) {
        // fetch guest cart
        Cart guestCart = cartRepository.findByUserId(guestId);
        // fetch user cart
        Cart userCart = cartRepository.findByUserId(userId);
    
        if (guestCart != null && userCart == null) {
            // Assign guest cart to user
            guestCart.setUserId(userId);
            cartRepository.save(guestCart);
            updateCart(guestCart);
            return convertToCartDTO(guestCart);
        } else if (guestCart != null && userCart != null) {
            // Merge guest cart items into user cart
            List<CartItem> guestItems = guestCart.getItems();
            List<CartItem> userItems = userCart.getItems();
    
            for (CartItem guestItem : guestItems) {
                boolean itemExists = false;
                for (CartItem userItem : userItems) {
                    if (guestItem.getId().equals(userItem.getId())) {
                        userItem.setQuantity(userItem.getQuantity() + guestItem.getQuantity());
                        itemExists = true;
                        break;
                    }
                }
                if (!itemExists) {
                    userItems.add(guestItem);
                }
            }
    
            // Update user cart and clear guest cart
            updateCart(userCart);
            cartRepository.save(userCart);
    
            guestCart.setItems(new ArrayList<>());
            updateCart(guestCart);
            cartRepository.save(guestCart);
    
            return convertToCartDTO(userCart);
        }
    
        // If no guest cart, return the user's cart or null
        return userCart != null ? convertToCartDTO(userCart) : null;
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
        dto.setImage(cartItem.getImage());
        return dto;
    }
}
