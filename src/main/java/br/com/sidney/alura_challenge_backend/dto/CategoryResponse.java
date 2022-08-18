package br.com.sidney.alura_challenge_backend.dto;

import br.com.sidney.alura_challenge_backend.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CategoryResponse {
    private Category category;
    private BigDecimal amount;
}
