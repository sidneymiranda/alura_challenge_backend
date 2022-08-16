package br.com.sidney.alura_challenge_backend.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ExpenseRequest {

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Value is required")
    private String value;

    @NotBlank(message = "Date is required")
    private String date;

    private String category;

}
