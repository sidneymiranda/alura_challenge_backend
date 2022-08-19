package br.com.sidney.alura_challenge_backend.service;

import br.com.sidney.alura_challenge_backend.dto.CategoryResponse;
import br.com.sidney.alura_challenge_backend.dto.ResumeResponse;
import br.com.sidney.alura_challenge_backend.repository.ExpenseRepository;
import br.com.sidney.alura_challenge_backend.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ResumeService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    public ResumeService(IncomeRepository incomeRepository, ExpenseRepository expenseRepository) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
    }

    public ResumeResponse getResume(Integer year, Integer month) {
        BigDecimal amountIncome = this.incomeRepository.amountByMonth(year, month);
        BigDecimal amountExpense = this.expenseRepository.amountByMonth(year, month);
        List<CategoryResponse> totalByCategory = this.expenseRepository.amountByCategory(year, month);

        amountIncome = amountIncome == null ? BigDecimal.ZERO : amountIncome;
        amountExpense = amountExpense == null ? BigDecimal.ZERO : amountExpense;

        return ResumeResponse.builder()
                .amountIncome(amountIncome)
                .amountExpense(amountExpense)
                .endBalance(amountIncome.subtract(amountExpense))
                .totalByCategory(totalByCategory)
                .build();
    }
}
