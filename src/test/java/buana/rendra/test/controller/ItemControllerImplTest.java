package buana.rendra.test.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import buana.rendra.test.controller.impl.ItemController;
import buana.rendra.test.dto.SuccessResponse;
import buana.rendra.test.entity.Item;
import buana.rendra.test.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

 class ItemControllerImplTest {

    @Mock
    private ItemServiceImpl itemService;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateItem_Success() {
        Item item = new Item(1L, "Item 1", new BigDecimal("10000.00"));
        SuccessResponse<Item> successResponse = new SuccessResponse<>(200, "Item created successfully", item);

        when(itemService.create(item)).thenReturn(successResponse);

        ResponseEntity<SuccessResponse<Item>> responseEntity = itemController.create(item);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(successResponse, responseEntity.getBody());

        verify(itemService, times(1)).create(item);
    }

    @Test
    void testUpdateItem_Success() {
        Long itemId = 1L;
        Item item = new Item(itemId, "Updated Item", new BigDecimal("20000.00"));
        SuccessResponse<Item> successResponse = new SuccessResponse<>(200, "Item updated successfully", item);

        when(itemService.update(itemId, item)).thenReturn(successResponse);

        ResponseEntity<SuccessResponse<Item>> responseEntity = itemController.update(itemId, item);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(successResponse, responseEntity.getBody());

        verify(itemService, times(1)).update(itemId, item);
    }

    @Test
    void testDeleteItem_Success() {
        Long itemId = 1L;

        ResponseEntity<Void> responseEntity = itemController.delete(itemId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        verify(itemService, times(1)).delete(itemId);
    }

    @Test
    void testGetAllItems_Success() {
        List<Item> items = List.of(new Item(1L, "Item 1", new BigDecimal("10000.00")),
                new Item(2L, "Item 2", new BigDecimal("15000.00")));

        SuccessResponse<List<Item>> successResponse = new SuccessResponse<>(200, "Items retrieved successfully", items);

        when(itemService.getAll()).thenReturn(successResponse);

        ResponseEntity<SuccessResponse<List<Item>>> responseEntity = itemController.getAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(successResponse, responseEntity.getBody());

        verify(itemService, times(1)).getAll();
    }
}

