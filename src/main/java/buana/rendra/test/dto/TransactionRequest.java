package buana.rendra.test.dto;

import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TransactionRequest {

    @NotEmpty(message = "Items list cannot be empty")
    @OneToMany
    private List<Long> itemIds;

}