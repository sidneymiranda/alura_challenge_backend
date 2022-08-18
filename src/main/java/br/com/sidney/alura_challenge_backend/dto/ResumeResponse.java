package br.com.sidney.alura_challenge_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class ResumeResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "amount_income")
    private BigDecimal amountIncome;

    @JsonProperty(value = "amount_expense")
    private BigDecimal amountExpense;

    @JsonProperty(value = "end_balance")
    private BigDecimal endBalance;

    @JsonProperty(value = "total_by_category")
    private List<CategoryResponse> totalByCategory;
}
