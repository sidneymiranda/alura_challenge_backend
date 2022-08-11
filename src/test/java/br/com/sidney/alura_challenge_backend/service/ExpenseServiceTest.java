package br.com.sidney.alura_challenge_backend.service;

import br.com.sidney.alura_challenge_backend.dto.ExpenseRequest;
import br.com.sidney.alura_challenge_backend.dto.ExpenseResponse;
import br.com.sidney.alura_challenge_backend.model.Expense;
import br.com.sidney.alura_challenge_backend.repository.ExpenseRepository;
import br.com.sidney.alura_challenge_backend.utils.DateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    @DisplayName("Should save expense")
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
    @DisplayName("Should not save expense that containing the same description within the same month")
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

    @Test
    @DisplayName("Should return all saved expenses")
    void thenGetAll_thenReturnAllIncomes() {
        ExpenseRequest curse = new ExpenseRequest();
        curse.setDate("12/03/2022 13:00");
        curse.setDescription("Curso Design Pattern");
        curse.setValue("99.90");

        ExpenseRequest internet = new ExpenseRequest();
        internet.setDate("15/03/2022 16:00");
        internet.setDescription("Conta de internet");
        internet.setValue("199.90");

        final List<Expense> expenses = Arrays.asList(new Expense(curse), new Expense(internet));
        when(repository.findAll()).thenReturn(expenses);

        final List<ExpenseResponse> incomeResponseList = service.getAll();

        assertEquals(2, incomeResponseList.size());
    }

    @Test
    @DisplayName("Should return expense through id")
    void whenIncomeFindById_thenReturnOneIncome() {
        ExpenseRequest internet = new ExpenseRequest();
        internet.setDate("10/08/2022 11:00");
        internet.setDescription("Conta de internet");
        internet.setValue("199.90");

        Expense expense = new Expense(internet);
        expense.setId(1L);

        when(repository.existsById(any(Long.class))).thenReturn(Boolean.TRUE);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(expense));

        final Optional<ExpenseResponse> response = service.findById("1");

        assertEquals(expense.getId().toString(), response.get().getId());
    }

    @Test
    @DisplayName("Should update expense")
    void whenUpdateValidIncome_thenOk() {
        Expense expense = new Expense();
        expense.setId(3L);
        expense.setDescription("Visa card");
        expense.setDate(DateUtils.stringToDate("08/08/2022 18:00"));
        expense.setValue(new BigDecimal("2500.00"));

        Expense updatedVisaCard = new Expense();
        updatedVisaCard.setValue(new BigDecimal("3500.00"));
        updatedVisaCard.setDescription("Visa card 2");
        updatedVisaCard.setDate(DateUtils.stringToDate("08/08/2022 18:00"));

        when(repository.findByDescription(any(String.class))).thenReturn(Optional.empty());
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(expense));
        when(repository.saveAndFlush(any(Expense.class))).thenReturn(updatedVisaCard);

        ExpenseRequest request = new ExpenseRequest();
        request.setDescription("Visa card 2");
        request.setValue("3500.00");
        request.setDate("18/08/2022 18:00");

        ExpenseResponse response = service.update("3", request);

        assertAll(
                () -> assertDoesNotThrow(() -> {}),
                () -> assertEquals(request.getValue(), response.getValue()),
                () -> assertNotEquals(expense.getValue(), new BigDecimal(response.getValue()))
        );
    }

    @Test
    @DisplayName("Should not update expense")
    void whenUpdateIncomeWithDescriptionInTheSameMonth_thenThrowException() {
        Expense expense = new Expense();
        expense.setId(3L);
        expense.setDescription("Visa card");
        expense.setDate(DateUtils.stringToDate("08/08/2022 18:00"));
        expense.setValue(new BigDecimal("2500.00"));

        when(repository.findByDescription(any(String.class))).thenReturn(Optional.of(expense));

        ExpenseRequest request = new ExpenseRequest();
        request.setDescription("Visa card");
        request.setValue("3500.00");
        request.setDate("18/08/2022 18:00");

        final ValidationException validationException = assertThrows(ValidationException.class, () -> service.update("3", request));
        assertEquals("Expense cannot be updated, was registered in the current month", validationException.getMessage());
    }

    @Test
    @DisplayName("Should delete expense by id")
    void whenDeleteById_thenOk() {
        when(repository.existsById(2L)).thenReturn(true);
        doNothing().when(repository).deleteById(any(Long.class));

        service.delete("2");
        assertDoesNotThrow(() -> {});
    }

    @Test
    @DisplayName("Should delete expense by id")
    void whenDeleteById_thenThrowsException() {
        when(repository.existsById(2L)).thenReturn(false);
        doNothing().when(repository).deleteById(any(Long.class));


        final NoSuchElementException validationException = assertThrows(NoSuchElementException.class, () -> service.delete("2"));

        assertEquals("Expense not exist by ID 2", validationException.getMessage());
    }

}