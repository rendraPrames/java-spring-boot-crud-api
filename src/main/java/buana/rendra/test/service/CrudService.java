package buana.rendra.test.service;

import buana.rendra.test.dto.SuccessResponse;
import java.util.List;

public interface CrudService<T, U> {

    SuccessResponse<T> create(T entity);

    SuccessResponse<T> update(U id, T entity);

    void delete(U id);

    SuccessResponse<List<T>> getAll();
}