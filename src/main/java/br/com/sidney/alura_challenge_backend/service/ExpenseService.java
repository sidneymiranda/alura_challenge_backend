package br.com.sidney.alura_challenge_backend.service;

import br.com.sidney.alura_challenge_backend.dto.ExpenseRequest;
import br.com.sidney.alura_challenge_backend.dto.ExpenseResponse;
import br.com.sidney.alura_challenge_backend.model.Expense;
import br.com.sidney.alura_challenge_backend.repository.ExpenseRepository;
import br.com.sidney.alura_challenge_backend.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
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


    public Optional<ExpenseResponse> findById(String id) {
        return this.repository.findById(Long.parseLong(id)).map(ExpenseResponse::new);
    }

    public ExpenseResponse update(String id, ExpenseRequest expenseRequest) {
        if (validateRecordDate(expenseRequest.getDescription(), expenseRequest.getDate()))
            throw new ValidationException("Expense cannot be updated, was registered in the current month");

        Expense expense = this.repository.findById(Long.parseLong(id))
                .orElseThrow(() -> new NoSuchElementException(""));

        BeanUtils.copyProperties(expenseRequest, expense);

        return new ExpenseResponse(this.repository.saveAndFlush(expense));
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
