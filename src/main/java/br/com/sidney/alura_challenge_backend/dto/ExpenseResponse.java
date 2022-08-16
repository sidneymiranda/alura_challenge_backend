package br.com.sidney.alura_challenge_backend.dto;

import br.com.sidney.alura_challenge_backend.model.Expense;
import br.com.sidney.alura_challenge_backend.utils.DateUtils;
import lombok.Data;

@Data
public class ExpenseResponse {

    private String id;
    private String description;
    private String value;
    private String date;
    private String category;

    public ExpenseResponse() {
    }

    public ExpenseResponse(Expense income) {
        this.id = String.valueOf(income.getId());
        this.description = income.getDescription();
        this.value = String.valueOf(income.getValue());
        this.date = DateUtils.dateToString(income.getDate());
        this.category = income.getCategory().getDescription();
    }
}
