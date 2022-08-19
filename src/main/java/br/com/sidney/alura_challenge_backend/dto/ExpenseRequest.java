package br.com.sidney.alura_challenge_backend.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ExpenseRequest {

    @NotBlank(message = "must not be blank")
    private String description;

    @NotBlank(message = "must not be blank")
    private String value;

    @NotBlank(message = "must not be blank")
    private String date;

    private String category;

}
