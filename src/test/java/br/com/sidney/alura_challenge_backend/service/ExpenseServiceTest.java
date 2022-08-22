package br.com.sidney.alura_challenge_backend.service;

import br.com.sidney.alura_challenge_backend.dto.ExpenseRequest;
import br.com.sidney.alura_challenge_backend.dto.ExpenseResponse;
import br.com.sidney.alura_challenge_backend.enums.Category;
import br.com.sidney.alura_challenge_backend.exceptions.ResourceNotFoundException;
import br.com.sidney.alura_challenge_backend.exceptions.ValidationException;
import br.com.sidney.alura_challenge_backend.model.Expense;
import br.com.sidney.alura_challenge_backend.repository.ExpenseRepository;
import br.com.sidney.alura_challenge_backend.utils.DateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Writing assertions for expense services")
class ExpenseServiceTest {

    private static ExpenseService service;
    private static ExpenseRepository repository;
    private static ExpenseRequest expenseRequest;
    static final List<Expense> expenses = new ArrayList<>();

    @BeforeAll
    public static void init() {
        repository = mock(ExpenseRepository.class);
        service = new ExpenseService(repository);

        expenseRequest = ExpenseRequest.builder()
                .date("11/08/2022")
                .value("985.56")
                .description("Cleaning products")
                .category("DWELLING_HOUSE")
                .build();
        expenses.add(new Expense(expenseRequest));

        expenseRequest = ExpenseRequest.builder()
                .date("10/08/2022")
                .value("199.90")
                .description("Fuel expense")
                .category("TRANSPORT")
                .build();
        expenses.add(new Expense(expenseRequest));

        expenseRequest = ExpenseRequest.builder()
                .date("08/08/2022")
                .value("2099.90")
                .description("Cloud Microservices Course")
                .category("EDUCATION")
                .build();
        expenses.add(new Expense(expenseRequest));

        expenseRequest = ExpenseRequest.builder()
                .date("01/09/2022")
                .value("699.90")
                .description("AWS Certified Cloud Practitioner")
                .category("EDUCATION")
                .build();
        expenses.add(new Expense(expenseRequest));
    }

    @Test
    @DisplayName("Should save expense")
    void whenCategoryIsValid_thenCreated() {
        when(repository.findByDescription(any(String.class))).thenReturn(Optional.empty());
        when(repository.save(any(Expense.class))).thenReturn(expenses.get(0));

        ExpenseResponse response = service.register(expenseRequest);

        assertNotNull(response);
    }

    @Test
    @DisplayName("Should save expense with category default - Others")
    void whenCategoryIsNull_thenSetCategoryDefaultAndCreated() {
        ExpenseRequest request = ExpenseRequest.builder()
                .date("16/12/2022")
                .value("320.56")
                .description("Nubank card")
                .build();

        when(repository.findByDescription(any(String.class))).thenReturn(Optional.empty());
        when(repository.save(any(Expense.class))).thenReturn(new Expense(request));

        ExpenseResponse response = service.register(expenseRequest);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(Category.OTHERS.getDescription(), response.getCategory())
        );
    }

    @Test
    @DisplayName("Should not save expense that containing the same description within the same month")
    void whenExpenseWithDescriptionAndSameMonth_thenNotSave() {
        ExpenseRequest request = ExpenseRequest.builder()
                .date("11/08/2022")
                .value("685.56")
                .description("Cleaning products")
                .category("DWELLING_HOUSE")
                .build();

        Expense cleaningProducts = expenses.get(0);

        when(repository.findByDescription(any(String.class))).thenReturn(Optional.of(cleaningProducts));

        final ValidationException validationException = assertThrows(ValidationException.class,
                () -> service.register(request));

        assertEquals("Expense already recorded this month", validationException.getMessage());
    }

    @Test
    @DisplayName("Should return all saved expenses")
    void thenGetAll_thenReturnAllExpense() {
        when(repository.findAll()).thenReturn(expenses);

        final List<ExpenseResponse> expenses = service.getAll(Optional.empty());

        assertEquals(4, expenses.size());
    }

    @Test
    @DisplayName("Should return all expenses with match description")
    void whenGetAllByDescription_thenReturnMatchExpense() {
        String param = "cloud";
        List<Expense> filteredList = expenses.stream()
                .filter(income -> income.getDescription().toLowerCase().contains(param.toLowerCase()))
                .collect(Collectors.toList());
        when(repository.findByDescriptionContainingIgnoreCase(param)).thenReturn(filteredList);

        final List<ExpenseResponse> expenses = service.getAll(Optional.of(param));

        assertEquals(2, expenses.size());
    }

    @Test
    @DisplayName("Should return expense through id")
    void whenExpenseFindById_thenReturnOneExpense() {
        Expense expense = expenses.get(1);
        expense.setId(2L);

        when(repository.existsById(any(Long.class))).thenReturn(Boolean.TRUE);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(expense));

        final Optional<ExpenseResponse> response = service.findById("2");

        assertEquals(expense.getId().toString(), response.get().getId());
    }

    @Test
    @DisplayName("Should update expense")
    void whenUpdateValidExpense_thenOk() {
        Expense expense = expenses.get(0);
        expense.setId(1L);

        Expense updated = new Expense();
        updated.setId(1L);
        updated.setValue(new BigDecimal("985.56"));
        updated.setDescription("Electronics and Cleaning products");
        updated.setDate(DateUtils.stringToDate("11/08/2022"));
        updated.setCategory(Category.DWELLING_HOUSE);

        when(repository.findByDescription(any(String.class))).thenReturn(Optional.empty());
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(expense));
        when(repository.saveAndFlush(any(Expense.class))).thenReturn(updated);

        ExpenseRequest request = ExpenseRequest.builder()
                .date("11/08/2022")
                .value("985.56")
                .description("Electronics and Cleaning products")
                .category("DWELLING_HOUSE")
                .build();

        ExpenseResponse response = service.update("1", request);

        assertAll(
                () -> assertDoesNotThrow(() -> {}),
                () -> assertEquals(request.getDescription(), response.getDescription())
        );
    }

    @Test
    @DisplayName("Should not update expense")
    void whenUpdateExpenseWithDescriptionInTheSameMonth_thenThrowException() {
        Expense expense = new Expense();
        expense.setId(3L);
        expense.setDescription("Visa card");
        expense.setDate(DateUtils.stringToDate("08/08/2022"));
        expense.setValue(new BigDecimal("2500.00"));

        when(repository.findByDescription(any(String.class))).thenReturn(Optional.of(expense));

        ExpenseRequest request = ExpenseRequest.builder()
                .date("08/08/2022")
                .description("Curso Design Pattern")
                .value("99.90").build();

        final ValidationException validationException =
                assertThrows(ValidationException.class, () -> service.update("3", request));
        assertEquals(
                "Expense cannot be updated, was registered in the current month",
                validationException.getMessage());
    }

    @Test
    @DisplayName("Should delete expense by id")
    void whenDeleteById_thenOk() {
        when(repository.existsById(2L)).thenReturn(Boolean.TRUE);
        doNothing().when(repository).deleteById(any(Long.class));

        service.delete("2");
        assertDoesNotThrow(() -> {});
    }

    @Test
    @DisplayName("Should delete expense by id")
    void whenDeleteById_thenThrowsException() {
        when(repository.existsById(2L)).thenReturn(false);
        doNothing().when(repository).deleteById(any(Long.class));

        final ResourceNotFoundException validationException =
                assertThrows(ResourceNotFoundException.class, () -> service.delete("2"));

        assertEquals("Expense not exist by ID 2", validationException.getMessage());
    }

    @Test
    @DisplayName("Should return expense from the same month")
    void whenFindByMonth_thenReturnExpensesForTheMonthInformed() {
        Integer year = 2022, month = 9;
        List<Expense> matchers = expenses.stream()
                .filter(income ->
                        Objects.equals(income.getDate().getMonth().getValue(), Month.SEPTEMBER.getValue())
                                && Objects.equals(income.getDate().getYear(), Year.of(year).getValue()))
                .collect(Collectors.toList());

        when(repository.findByMonth(year, month)).thenReturn(matchers);
        final List<ExpenseResponse> incomeResponseList = service.findByMonth(year, month);
        assertEquals(1, incomeResponseList.size());
    }

    @Test
    @DisplayName("Should not save a new expense with incorrect date")
    void whenSaveWithIncorrectDate_thenBadRequest() {
        final DateTimeParseException validationException = assertThrows(
                DateTimeParseException.class,
                () -> service.register(ExpenseRequest.builder()
                        .description("iFood")
                        .value("95.50")
                        .date("22/08/22").build()));

        assertEquals("Text '22/08/22' could not be parsed at index 6", validationException.getMessage());
    }

}