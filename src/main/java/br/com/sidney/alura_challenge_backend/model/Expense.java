package br.com.sidney.alura_challenge_backend.model;

import br.com.sidney.alura_challenge_backend.dto.ExpenseRequest;
import br.com.sidney.alura_challenge_backend.enums.Category;
import br.com.sidney.alura_challenge_backend.utils.DateUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Expense implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Category category;

    public Expense() { }

    public Expense(ExpenseRequest request) {
        this.description = request.getDescription();
        this.value = new BigDecimal(request.getValue()).setScale(2, RoundingMode.UP);
        this.date = DateUtils.stringToDate(request.getDate());
        this.category = categoryVerify(request.getCategory());
    }

    private Category categoryVerify(String category) {
        return category == null ? Category.OTHERS : Category.valueOf(category.toUpperCase());
    }
}
