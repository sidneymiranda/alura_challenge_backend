package br.com.sidney.alura_challenge_backend.service;

import br.com.sidney.alura_challenge_backend.dto.ExpenseRequest;
import br.com.sidney.alura_challenge_backend.dto.ExpenseResponse;
import br.com.sidney.alura_challenge_backend.model.Expense;
import br.com.sidney.alura_challenge_backend.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ValidationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Writing assertions for expense services")
class ExpenseServiceTest {

    private static ExpenseService service;

    private static ExpenseRepository repository;

    @BeforeAll
    public static void init() {
        repository = mock(ExpenseRepository.class);
        service = new ExpenseService(repository);
    }

    @Test
    @DisplayName("Should save income")
    void whenRegister_thenSave() {
        ExpenseRequest request = new ExpenseRequest();
        request.setDate("08/08/2022 18:00");
        request.setDescription("Curso Design Pattern");
        request.setValue("99.90");

        Expense income = new Expense(request);
        when(repository.save(any(Expense.class))).thenReturn(income);

        ExpenseResponse response = service.register(request);

        assertNotNull(response);
    }

    @Test
    @DisplayName("Should not save income that containing the same description within the same month")
    void whenIncomeWithDescriptionAndSameMonth_thenNotSave() {
        ExpenseRequest request = new ExpenseRequest();
        request.setDate("08/08/2022 18:00");
        request.setDescription("Curso Design Pattern");
        request.setValue("99.90");

        Expense income = new Expense(request);

        when(repository.findByDescription(any(String.class))).thenReturn(Optional.of(income));

        final ValidationException validationException = assertThrows(ValidationException.class, () -> service.register(request));

        assertEquals("The expense already registered for the informed month", validationException.getMessage());
    }


}