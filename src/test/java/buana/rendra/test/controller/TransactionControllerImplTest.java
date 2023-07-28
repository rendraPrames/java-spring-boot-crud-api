package buana.rendra.test.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import buana.rendra.test.controller.impl.TransactionController;
import buana.rendra.test.dto.SuccessResponse;
import buana.rendra.test.dto.TransactionRequest;
import buana.rendra.test.entity.Item;
import buana.rendra.test.entity.Transaction;
import buana.rendra.test.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

 class TransactionControllerImplTest {

    @Mock
    private TransactionServiceImpl transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction_Success() {
        List<Long> itemIds = List.of(1L, 2L, 3L);
        List<Item> items = List.of(
                new Item(1L, "Item 1", new BigDecimal("10.00")),
                new Item(2L, "Item 2", new BigDecimal("20.00")),
                new Item(3L, "Item 3", new BigDecimal("15.00"))
        );
        TransactionRequest transactionRequest = new TransactionRequest(itemIds);
        Transaction transaction = new Transaction(1L, items);

        SuccessResponse<Transaction> successResponse = new SuccessResponse<>(201, "Transaction created successfully", transaction);

        when(transactionService.createTransaction(transactionRequest)).thenReturn(successResponse);

        ResponseEntity<SuccessResponse<Transaction>> responseEntity = transactionController.create(transactionRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(successResponse, responseEntity.getBody());

        verify(transactionService, times(1)).createTransaction(transactionRequest);
    }

    @Test
    void testUpdateTransaction_Success() {
        Long transactionId = 1L;
        List<Long> itemIds = List.of(1L, 2L);
        List<Item> items = List.of(
                new Item(1L, "Updated Item 1", new BigDecimal("25.00")),
                new Item(2L, "Updated Item 2", new BigDecimal("30.00"))
        );
        TransactionRequest transactionRequest = new TransactionRequest(itemIds);
        Transaction transaction = new Transaction(transactionId, items);

        SuccessResponse<Transaction> successResponse = new SuccessResponse<>(200, "Transaction updated successfully", transaction);

        when(transactionService.update(transactionId, transaction)).thenReturn(successResponse);

        ResponseEntity<SuccessResponse<Transaction>> responseEntity = transactionController.update(transactionId, transaction);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(successResponse, responseEntity.getBody());

        verify(transactionService, times(1)).update(transactionId, transaction);
    }

    @Test
    void testDeleteTransaction_Success() {
        Long transactionId = 1L;

        ResponseEntity<Void> responseEntity = transactionController.delete(transactionId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        verify(transactionService, times(1)).delete(transactionId);
    }

    @Test
    void testGetAllTransactions_Success() {
        List<Transaction> transactions = List.of(
                new Transaction(1L, List.of(new Item(1L, "Item 1", new BigDecimal("10.00")))),
                new Transaction(2L, List.of(new Item(2L, "Item 2", new BigDecimal("20.00"))))
        );

        SuccessResponse<List<Transaction>> successResponse = new SuccessResponse<>(200, "Transactions retrieved successfully", transactions);

        when(transactionService.getAll()).thenReturn(successResponse);

        ResponseEntity<SuccessResponse<List<Transaction>>> responseEntity = transactionController.getAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(successResponse, responseEntity.getBody());

        verify(transactionService, times(1)).getAll();
    }

    @Test
    void testCalculateTotalPrice_Success() {
        Long transactionId = 1L;
        BigDecimal totalPrice = new BigDecimal("30.00");

        when(transactionService.calculateTotalPrice(transactionId)).thenReturn(totalPrice);

        ResponseEntity<BigDecimal> responseEntity = transactionController.calculateTotalPrice(transactionId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(totalPrice, responseEntity.getBody());

        verify(transactionService, times(1)).calculateTotalPrice(transactionId);
    }
}
