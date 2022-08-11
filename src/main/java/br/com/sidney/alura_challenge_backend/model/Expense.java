package br.com.sidney.alura_challenge_backend.model;

import br.com.sidney.alura_challenge_backend.dto.ExpenseRequest;
import br.com.sidney.alura_challenge_backend.utils.DateUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Expense implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Description is required")
    private String description;

    @Column(nullable = false)
    @NotBlank(message = "Value is required")
    private BigDecimal value;

    @Column(nullable = false)
    @NotBlank(message = "Date is required")
    private LocalDateTime date;

    public Expense() {
    }

    public Expense(ExpenseRequest request) {
        this.description = request.getDescription();
        this.value = new BigDecimal(request.getValue()).setScale(2);
        this.date = DateUtils.stringToDate(request.getDate());
    }
}
