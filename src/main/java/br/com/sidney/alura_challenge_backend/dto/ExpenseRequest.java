package br.com.sidney.alura_challenge_backend.dto;

import lombok.Data;

@Data
public class ExpenseRequest {

    private String description;

    private String value;

    private String date;

}
