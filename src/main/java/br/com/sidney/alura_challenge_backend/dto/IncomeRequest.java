package br.com.sidney.alura_challenge_backend.dto;

import lombok.Data;

@Data
public class IncomeRequest {

    private String description;

    private String value;

    private String date;

}
