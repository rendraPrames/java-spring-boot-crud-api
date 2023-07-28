package buana.rendra.test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ItemRequest {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @PositiveOrZero(message = "Price must be a positive value or zero")
    private BigDecimal price;
}
