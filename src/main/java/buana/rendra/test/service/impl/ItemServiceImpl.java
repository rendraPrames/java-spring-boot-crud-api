package buana.rendra.test.service.impl;

import buana.rendra.test.dto.SuccessResponse;
import buana.rendra.test.entity.Item;
import buana.rendra.test.exception.NotFoundException;
import buana.rendra.test.repository.ItemRepository;
import buana.rendra.test.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements CrudService<Item, Long> {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public SuccessResponse<Item> create(Item entity) {
        Item savedItem = itemRepository.save(entity);
        return new SuccessResponse<>(201, "Item created successfully", savedItem);
    }

    @Override
    public SuccessResponse<Item> update(Long id, Item entity) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item existingItem = optionalItem.get();
            existingItem.setName(entity.getName());
            existingItem.setPrice(entity.getPrice());

            Item updatedItem = itemRepository.save(existingItem);
            return new SuccessResponse<>(200, "Item updated successfully", updatedItem);
        } else {
            throw new NotFoundException("Item not found with ID: " + id);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            itemRepository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException("Failed to delete item with ID: " + id);
        }
    }

    @Override
    public SuccessResponse<List<Item>> getAll() {
        try {
            List<Item> items = itemRepository.findAll();
            return new SuccessResponse<>(200, "Items retrieved successfully", items);
        } catch (Exception e) {
            throw new NotFoundException("Failed to retrieve items: " + e.getMessage());
        }
    }
}