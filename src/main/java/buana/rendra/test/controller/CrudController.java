package buana.rendra.test.controller;

import buana.rendra.test.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface CrudController<T, U> {

    @PostMapping
    @Operation(summary = "Add New Data")
    ResponseEntity<SuccessResponse<T>> create(@Valid @RequestBody T entity);

    @PutMapping("/{id}")
    @Operation(summary = "Update Data")
    ResponseEntity<SuccessResponse<T>> update(@PathVariable U id, @Valid @RequestBody T entity);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Data")
    ResponseEntity<Void> delete(@PathVariable U id);

    @GetMapping
    @Operation(summary = "Get All Data")
    ResponseEntity<SuccessResponse<List<T>>> getAll();
}