package br.com.sidney.alura_challenge_backend.service;

import br.com.sidney.alura_challenge_backend.dto.CategoryResponse;
import br.com.sidney.alura_challenge_backend.dto.ResumeResponse;
import br.com.sidney.alura_challenge_backend.enums.Category;
import br.com.sidney.alura_challenge_backend.repository.ExpenseRepository;
import br.com.sidney.alura_challenge_backend.repository.IncomeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResumeServiceTest {
    private ResumeService service;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    ResumeServiceTest() {
        this.incomeRepository = mock(IncomeRepository.class);
        this.expenseRepository = mock(ExpenseRepository.class);
        this.service = new ResumeService(incomeRepository, expenseRepository);
    }


    @Test
    @DisplayName("Should return: amount income, amount expense, end balance and amount by category for the informed month")
    void whenRequestResumeTheMonth_thenReturn() {
        List<CategoryResponse> totalByCategory = List.of(
                new CategoryResponse(Category.FOOD, BigDecimal.valueOf(1250.0)),
                new CategoryResponse(Category.HEALTH, BigDecimal.valueOf(350.0)),
                new CategoryResponse(Category.EDUCATION, BigDecimal.valueOf(2000.0)),
                new CategoryResponse(Category.OTHERS, BigDecimal.valueOf(8900.0))
        );

        BigDecimal amountIncome = BigDecimal.valueOf(8000.0);
        BigDecimal amountExpense = BigDecimal.valueOf(4490.0);
        BigDecimal endBalance = amountIncome.subtract(amountExpense);


        when(this.incomeRepository.amountByMonth(any(Integer.class), any(Integer.class))).thenReturn(amountIncome);
        when(this.expenseRepository.amountByMonth(any(Integer.class), any(Integer.class))).thenReturn(amountExpense);
        when(this.expenseRepository.amountByCategory(any(Integer.class), any(Integer.class))).thenReturn(totalByCategory);

        Integer year = 2022, month = 8;

        ResumeResponse expect = ResumeResponse.builder()
                .amountIncome(amountIncome)
                .amountExpense(amountExpense)
                .endBalance(endBalance)
                .totalByCategory(totalByCategory)
                .build();

        ResumeResponse resume = service.getResume(year, month);

        assertEquals(expect, resume);
    }
}