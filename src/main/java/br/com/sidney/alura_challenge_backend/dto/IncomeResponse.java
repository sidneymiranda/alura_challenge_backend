package br.com.sidney.alura_challenge_backend.dto;

import br.com.sidney.alura_challenge_backend.model.Income;
import br.com.sidney.alura_challenge_backend.utils.DateUtils;
import lombok.Data;

@Data
public class IncomeResponse {

    private String id;

    private String description;

    private String value;

    private String date;

    public IncomeResponse(Income income) {
        this.id = String.valueOf(income.getId());
        this.description = income.getDescription();
        this.value = String.valueOf(income.getValue());
        this.date = DateUtils.dateToString(income.getDate());
    }
}
