package br.com.sidney.alura_challenge_backend.service;

import br.com.sidney.alura_challenge_backend.dto.ExpenseRequest;
import br.com.sidney.alura_challenge_backend.dto.ExpenseResponse;
import br.com.sidney.alura_challenge_backend.model.Expense;
import br.com.sidney.alura_challenge_backend.model.Income;
import br.com.sidney.alura_challenge_backend.repository.ExpenseRepository;
import br.com.sidney.alura_challenge_backend.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final ExpenseRepository repository;

    @Autowired
    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public ExpenseResponse register(ExpenseRequest request) {
        if(validateRecordDate(request.getDescription(), request.getDate())) {
            throw new ValidationException("The expense already registered for the informed month");
        }

        Expense income = new Expense(request);

        income = this.repository.save(income);

        return new ExpenseResponse(income);
    }

    public List<ExpenseResponse> getAll() {
        return this.repository.findAll()
                .stream().map(ExpenseResponse::new)
                .collect(Collectors.toList());
    }

    private boolean validateRecordDate(String description, String date) {
        Optional<Expense> expense = this.repository.findByDescription(description);

        if(expense.isEmpty())
            return false;

        LocalDateTime register = expense.get().getDate();

        return register.getYear() == DateUtils.stringToDate(date).getYear()
                && register.getMonth().equals(DateUtils.stringToDate(date).getMonth());
    }

}
