package buana.rendra.test.service;

import buana.rendra.test.dto.SuccessResponse;
import buana.rendra.test.entity.Item;
import buana.rendra.test.exception.NotFoundException;
import buana.rendra.test.repository.ItemRepository;
import buana.rendra.test.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    Item baju = Item.builder()
            .id(1L)
            .name("Baju")
            .price(new BigDecimal(50000))
            .build();

    Item celana = Item.builder()
            .id(1L)
            .name("Celana")
            .price(new BigDecimal(50000))
            .build();
    @Test
    void testCreateItem() {
        // Given
        when(itemRepository.save(baju)).thenReturn(baju);

        // When
        SuccessResponse<Item> response = itemService.create(baju);

        // Then
        assertNotNull(response);
        assertEquals(201, response.getStatus());
        assertEquals("Item created successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(baju, response.getData());
    }

    @Test
    void testUpdateItem_Success() {
        // Given
        when(itemRepository.findById(baju.getId())).thenReturn(Optional.of(baju));
        when(itemRepository.save(baju)).thenReturn(celana);

        // When
        SuccessResponse<Item> response = itemService.update(baju.getId(), celana);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("Item updated successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(celana, response.getData());
    }

    @Test
    void testUpdateItem_ItemNotFound() {
        // Given
        when(itemRepository.findById(baju.getId())).thenReturn(Optional.empty());

        // When, Then
        assertThrows(NotFoundException.class, () -> itemService.update(baju.getId(), celana));
    }

    @Test
    void testDeleteItem_Success() {
        // Given
        Long itemId = 1L;

        // When
        itemService.delete(itemId);

        // Then
        verify(itemRepository, times(1)).deleteById(itemId);
    }

    @Test
    void testDeleteItem_ItemNotFound() {
        // Given
        Long itemId = 1L;

        doThrow(new RuntimeException()).when(itemRepository).deleteById(itemId);

        // When, Then
        assertThrows(NotFoundException.class, () -> itemService.delete(itemId));
    }

    @Test
    void testGetAllItems() {
        // Given
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L, "Item 1", new BigDecimal(50000)));
        items.add(new Item(2L, "Item 2", new BigDecimal(50000)));

        when(itemRepository.findAll()).thenReturn(items);

        // When
        SuccessResponse<List<Item>> response = itemService.getAll();

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("Items retrieved successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(items, response.getData());
    }

    @Test
    void testGetAll_Exception() {
        // Simulate that an exception occurs while fetching items
        when(itemRepository.findAll()).thenThrow(NotFoundException.class);

        // Call the method under test and expect NotFoundException
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            itemService.getAll();
        });

        assertEquals("Failed to retrieve items: null", exception.getMessage());

        verify(itemRepository, times(1)).findAll();
    }
}