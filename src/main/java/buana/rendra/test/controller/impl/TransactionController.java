package buana.rendra.test.controller.impl;

import buana.rendra.test.controller.CrudController;
import buana.rendra.test.dto.SuccessResponse;
import buana.rendra.test.dto.TransactionRequest;
import buana.rendra.test.entity.Transaction;
import buana.rendra.test.service.impl.TransactionServiceImpl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@OpenAPIDefinition(info = @Info(title = "My API", version = "1.0"))
public class TransactionController implements CrudController<Transaction, Long> {

    @Autowired
    private TransactionServiceImpl transactionService;

    @PostMapping("/v2")
    @Operation(summary = "Add New Data")
    public ResponseEntity<SuccessResponse<Transaction>> create(TransactionRequest entity) {
        return ResponseEntity.ok(transactionService.createTransaction(entity));
    }

    @Override
    public ResponseEntity<SuccessResponse<Transaction>> create(Transaction entity) {
        return null;
    }

    @Override
    public ResponseEntity<SuccessResponse<Transaction>> update(Long id, Transaction entity) {
        return ResponseEntity.ok(transactionService.update(id, entity));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<SuccessResponse<List<Transaction>>> getAll() {
        return ResponseEntity.ok(transactionService.getAll());
    }

    @GetMapping("/transactions/{id}/totalPrice")
    public ResponseEntity<BigDecimal> calculateTotalPrice(@PathVariable Long id) {
        BigDecimal totalPrice = transactionService.calculateTotalPrice(id);
        return new ResponseEntity<>(totalPrice, HttpStatus.OK);
    }
}