package buana.rendra.test.controller.impl;

import buana.rendra.test.controller.CrudController;
import buana.rendra.test.dto.SuccessResponse;
import buana.rendra.test.entity.Item;
import buana.rendra.test.service.impl.ItemServiceImpl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@OpenAPIDefinition(info = @Info(title = "My API", version = "1.0"))
public class ItemController implements CrudController<Item, Long> {

    @Autowired
    private ItemServiceImpl itemService;

    @Override
    public ResponseEntity<SuccessResponse<Item>> create(Item entity) {
        return ResponseEntity.ok(itemService.create(entity));
    }

    @Override
    public ResponseEntity<SuccessResponse<Item>> update(Long id, Item entity) {
        return ResponseEntity.ok(itemService.update(id, entity));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<SuccessResponse<List<Item>>> getAll() {
        return ResponseEntity.ok(itemService.getAll());
    }
}