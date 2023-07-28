package buana.rendra.test.service.impl;

import buana.rendra.test.dto.SuccessResponse;
import buana.rendra.test.dto.TransactionRequest;
import buana.rendra.test.entity.Item;
import buana.rendra.test.entity.Transaction;
import buana.rendra.test.exception.NotFoundException;
import buana.rendra.test.repository.ItemRepository;
import buana.rendra.test.repository.TransactionRepository;
import buana.rendra.test.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements CrudService<Transaction, Long> {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ItemRepository itemRepository;

    public SuccessResponse<Transaction> createTransaction(TransactionRequest request) {
        List<Long> itemIds = request.getItemIds();
        List<Item> items = itemIds.stream()
                .map(itemId -> itemRepository.findById(itemId).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        if (items.isEmpty()) {
            throw new NotFoundException("No valid items found in the list.");
        }

        Transaction transaction = new Transaction();
        transaction.setItems(items);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return new SuccessResponse<>(201, "Transaction created successfully", savedTransaction);
    }

    @Override
    public SuccessResponse<Transaction> create(Transaction entity) {
        return null;
    }

    @Override
    public SuccessResponse<Transaction> update(Long id, Transaction entity) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
        if (optionalTransaction.isPresent()) {
            Transaction existingTransaction = optionalTransaction.get();
            existingTransaction.setItems(entity.getItems());

            Transaction updatedTransaction = transactionRepository.save(existingTransaction);
            return new SuccessResponse<>(200, "Transaction updated successfully", updatedTransaction);
        } else {
            throw new NotFoundException("Transaction not found with ID: " + id);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            transactionRepository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException("Failed to delete transaction with ID: " + id);
        }
    }

    @Override
    public SuccessResponse<List<Transaction>> getAll() {
        try {
            List<Transaction> transactions = transactionRepository.findAll();
            return new SuccessResponse<>(200, "Transactions retrieved successfully", transactions);
        } catch (Exception e) {
            throw new NotFoundException("Failed to retrieve transactions: " + e.getMessage());
        }
    }

    public BigDecimal calculateTotalPrice(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction not found with ID: " + transactionId));

        List<Item> items = transaction.getItems();
        return items.stream()
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}