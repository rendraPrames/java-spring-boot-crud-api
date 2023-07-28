package buana.rendra.test.service;

import buana.rendra.test.dto.SuccessResponse;
import buana.rendra.test.dto.TransactionRequest;
import buana.rendra.test.entity.Item;
import buana.rendra.test.entity.Transaction;
import buana.rendra.test.exception.NotFoundException;
import buana.rendra.test.repository.ItemRepository;
import buana.rendra.test.repository.TransactionRepository;
import buana.rendra.test.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateTransaction_Success() {
        List<Long> itemIds = List.of(1L, 2L, 3L);
        List<Item> items = List.of(
                new Item(1L, "Item 1", new BigDecimal("10000.00")),
                new Item(2L, "Item 2", new BigDecimal("20000.00")),
                new Item(3L, "Item 3", new BigDecimal("30000.00"))
        );

        TransactionRequest request = new TransactionRequest();
        request.setItemIds(itemIds);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(items.get(0)));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(items.get(1)));
        when(itemRepository.findById(3L)).thenReturn(Optional.of(items.get(2)));

        // Set the behavior for transactionRepository.save() to assign an ID to the saved transaction
        when(transactionRepository.save(any())).thenAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            savedTransaction.setId(1L); // Or any other valid ID
            return savedTransaction;
        });

        BigDecimal expectedTotalPrice = new BigDecimal("60000.00");

        // Call the method under test
        SuccessResponse<Transaction> response = transactionService.createTransaction(request);

        assertEquals(201, response.getStatus());
        assertEquals("Transaction created successfully", response.getMessage());

        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void testCreateTransaction_EmptyItemsList() {
        TransactionRequest request = new TransactionRequest();
        request.setItemIds(new ArrayList<>());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            transactionService.createTransaction(request);
        });

        assertEquals("No valid items found in the list.", exception.getMessage());

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void testCreateTransaction_ItemNotFound() {
        List<Long> itemIds = List.of(1L, 2L);

        TransactionRequest request = new TransactionRequest();
        request.setItemIds(itemIds);

        Item item1 = new Item(1L, "Item 1", new BigDecimal("10000.00"));
        Item item2 = new Item(2L, "Item 2", new BigDecimal("20000.00"));

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));

        Transaction transaction = new Transaction();
        transaction.setItems(List.of(item1, item2));

        when(transactionRepository.save(any())).thenReturn(transaction);

        SuccessResponse<Transaction> response = transactionService.createTransaction(request);

        assertEquals(201, response.getStatus());
        assertEquals("Transaction created successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(transaction.getItems(), response.getData().getItems());

        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void testUpdateTransaction_Success() {
        Long transactionId = 1L;
        List<Long> itemIds = List.of(1L, 2L, 3L);
        List<Item> items = List.of(
                new Item(1L, "Item 1", new BigDecimal("10000.00")),
                new Item(2L, "Item 2", new BigDecimal("20000.00")),
                new Item(3L, "Item 3", new BigDecimal("30000.00"))
        );

        Transaction request = new Transaction();
        request.setItems(items);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(items.get(0)));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(items.get(1)));
        when(itemRepository.findById(3L)).thenReturn(Optional.of(items.get(2)));

        Transaction existingTransaction = new Transaction();
        existingTransaction.setId(transactionId);
        existingTransaction.setItems(items.subList(0, 2));

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setId(transactionId);
        updatedTransaction.setItems(items);

        when(transactionRepository.save(any())).thenReturn(updatedTransaction);

        BigDecimal expectedTotalPrice = new BigDecimal("60000.00");

        // Call the method under test
        SuccessResponse<Transaction> response = transactionService.update(transactionId, request);

        assertEquals(200, response.getStatus());
        assertEquals("Transaction updated successfully", response.getMessage());
        assertEquals(updatedTransaction, response.getData());
        assertEquals(expectedTotalPrice, transactionService.calculateTotalPrice(updatedTransaction.getId()));

        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void testUpdateTransaction_TransactionNotFound() {
        Long transactionId = 1L;
        List<Item> items = List.of(
                new Item(1L, "Item 1", new BigDecimal("10000.00")),
                new Item(2L, "Item 2", new BigDecimal("20000.00")),
                new Item(3L, "Item 3", new BigDecimal("30000.00"))
        );
        Transaction request = new Transaction();
        request.setItems(items);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            transactionService.update(transactionId, request);
        });

        assertEquals("Transaction not found with ID: 1", exception.getMessage());

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void testDelete_Success() {
        Long transactionId = 1L;

        // Call the method under test
        assertDoesNotThrow(() -> transactionService.delete(transactionId));

        // Verify that deleteById was called with the correct ID
        verify(transactionRepository, times(1)).deleteById(transactionId);
    }

    @Test
    void testDelete_TransactionNotFound() {
        Long transactionId = 1L;

        // Simulate that an exception occurs while deleting the transaction
        doThrow(NotFoundException.class).when(transactionRepository).deleteById(transactionId);

        // Call the method under test and assert that it throws NotFoundException
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            transactionService.delete(transactionId);
        });

        assertEquals("Failed to delete transaction with ID: " + transactionId, exception.getMessage());

        // Verify that deleteById was called with the correct ID
        verify(transactionRepository, times(1)).deleteById(transactionId);
    }

    @Test
    void testGetAll_Success() {
        List<Transaction> transactions = List.of(
                new Transaction(1L, List.of(
                        new Item(1L, "Item 1", new BigDecimal(10000)),
                        new Item(2L, "Item 2", new BigDecimal(20000))
                )),
                new Transaction(2L, List.of(
                        new Item(3L, "Item 3", new BigDecimal(30000))
                ))
        );

        // Mock the behavior of the transactionRepository.findAll() method
        when(transactionRepository.findAll()).thenReturn(transactions);

        // Call the method under test
        SuccessResponse<List<Transaction>> response = transactionService.getAll();

        assertEquals(200, response.getStatus());
        assertEquals("Transactions retrieved successfully", response.getMessage());
        assertEquals(transactions, response.getData());

        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testGetAll_TransactionNotFound() {
        // Simulate that an exception occurs while fetching transactions
        when(transactionRepository.findAll()).thenThrow(NotFoundException.class);

        // Call the method under test and assert that it throws NotFoundException
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            transactionService.getAll();
        });

        assertEquals("Failed to retrieve transactions: null", exception.getMessage());

        verify(transactionRepository, times(1)).findAll();
    }
}
