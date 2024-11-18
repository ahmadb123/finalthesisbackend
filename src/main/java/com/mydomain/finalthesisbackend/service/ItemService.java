package com.mydomain.finalthesisbackend.service;

import com.mydomain.finalthesisbackend.model.Item;
import com.mydomain.finalthesisbackend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    public Item getItemById(String id) {
        return itemRepository.findById(id).orElse(null);
    }

    public void deleteItem(String id) {
        itemRepository.deleteById(id);
    }
}
