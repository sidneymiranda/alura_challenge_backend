package br.com.sidney.alura_challenge_backend.dto;

import br.com.sidney.alura_challenge_backend.validators.interfaces.Date;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class IncomeRequest {

    @NotBlank(message = "must not be blank")
    private String description;

    @NotBlank(message = "must not be blank")
    private String value;

    @Date
    @NotBlank(message = "must not be blank")
    private String date;

}
