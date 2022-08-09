package br.com.sidney.alura_challenge_backend.model;

import br.com.sidney.alura_challenge_backend.dto.IncomeRequest;
import br.com.sidney.alura_challenge_backend.utils.DateUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Income implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Description is required")
    private String description;

    @Column(nullable = false)

    @NotBlank(message = "Value is required")
    private BigDecimal value;

    @NotBlank(message = "Date is required")
    @Column(nullable = false)
    private LocalDateTime date;

    public Income() {

    }

    public Income(IncomeRequest request) {
        this.description = request.getDescription();
        this.value = new BigDecimal(request.getValue()).setScale(2);
        this.date = DateUtils.stringToDate(request.getDate());
    }
}
