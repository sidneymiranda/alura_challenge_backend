package br.com.sidney.alura_challenge_backend.service;

import br.com.sidney.alura_challenge_backend.dto.IncomeRequest;
import br.com.sidney.alura_challenge_backend.dto.IncomeResponse;
import br.com.sidney.alura_challenge_backend.model.Income;
import br.com.sidney.alura_challenge_backend.repository.IncomeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ValidationException;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Writing assertions for income services")
class IncomeServiceTest {

    private static IncomeService service;

    private static IncomeRepository repository;
    private static IncomeRequest incomeRequest;
    static final List<Income> incomes = new ArrayList<>();

    @BeforeAll
    public static void init() {
        repository = mock(IncomeRepository.class);
        service = new IncomeService(repository);

        incomeRequest = IncomeRequest.builder()
                .date("08/08/2022")
                .value("5599.90")
                .description("Salary")
                .build();
        incomes.add(new Income(incomeRequest));

        incomeRequest = IncomeRequest.builder()
                .date("10/08/2022")
                .value("199.90")
                .description("Loan")
                .build();
        incomes.add(new Income(incomeRequest));

        incomeRequest = IncomeRequest.builder()
                .date("18/10/2022")
                .value("2100.00")
                .description("Loan")
                .build();
        incomes.add(new Income(incomeRequest));

        incomeRequest = IncomeRequest.builder()
                .date("18/11/2022")
                .value("3100.00")
                .description("iPhone 12")
                .build();
        incomes.add(new Income(incomeRequest));

        incomeRequest = IncomeRequest.builder()
                .date("18/11/2022")
                .value("6500.00")
                .description("iPhone 13")
                .build();
        incomes.add(new Income(incomeRequest));

        incomeRequest = IncomeRequest.builder()
                .date("18/11/2022")
                .value("8500.00")
                .description("iPhone 13 Pro")
                .build();
        incomes.add(new Income(incomeRequest));
    }

    @Test
    @DisplayName("Should save income")
    void whenRegister_thenSave() {
        incomeRequest = IncomeRequest.builder()
                .date("08/08/2022")
                .value("5990.90")
                .description("Salary")
                .build();
        Income income = incomes.get(0);
        when(repository.save(any(Income.class))).thenReturn(income);

        IncomeResponse response = service.register(incomeRequest);

        assertNotNull(response);
    }

    @Test
    @DisplayName("Should not save income that containing the same description within the same month")
    void whenIncomeWithDescriptionAndSameMonth_thenNotSave() {
        incomeRequest = IncomeRequest.builder()
                .date("15/08/2022")
                .value("3099.90")
                .description("Loan")
                .build();

        Income income = incomes.get(1);

        when(repository.findByDescription(any(String.class))).thenReturn(Optional.of(income));

        final ValidationException validationException = assertThrows(ValidationException.class, () -> service.register(incomeRequest));

        assertEquals("The income already registered for the informed month", validationException.getMessage());
    }

    @Test
    @DisplayName("Should return all saved incomes")
    void whenGetAllByDescription_thenReturnMatchIncomes() {
        when(repository.findAll()).thenReturn(incomes);
        final List<IncomeResponse> incomeResponseList = service.getAll(Optional.empty());
        assertEquals(6, incomeResponseList.size());
    }

    @Test
    @DisplayName("Should return all incomes with match description")
    void whenGetAllByDescription_thenReturnAllMatchIncomes() {
        String param = "Loan";
        List<Income> filteredList = incomes.stream()
                .filter(income -> income.getDescription().equals(param))
                .collect(Collectors.toList());

        when(repository.findByDescriptionContainingIgnoreCase(param)).thenReturn(filteredList);
        final List<IncomeResponse> incomeResponseList = service.getAll(Optional.of(param));

        assertEquals(2, incomeResponseList.size());
    }

    @Test
    @DisplayName("Should return all incomes with match description")
    void whenGetAllByDescription_thenReturnAllIncomesWithContainsTheKeywordInDescription() {
        String param = "phonE";

        List<Income> filteredList = incomes.stream()
                .filter(income -> income.getDescription().toLowerCase().contains(param.toLowerCase()))
                .collect(Collectors.toList());

        when(repository.findByDescriptionContainingIgnoreCase(param)).thenReturn(filteredList);
        final List<IncomeResponse> incomeResponseList = service.getAll(Optional.of(param));

        assertEquals(3, incomeResponseList.size());
    }

    @Test
    @DisplayName("Should return income through id")
    void whenIncomeFindById_thenReturnOneIncome() {
        Income income = incomes.get(0);
        income.setId(1L);

        when(repository.existsById(any(Long.class))).thenReturn(Boolean.TRUE);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(income));

        final Optional<IncomeResponse> response = service.findById("1");

        assertEquals(income.getId().toString(), response.get().getId());
    }

    @Test
    @DisplayName("Should delete income through id")
    void whenExistsIncomeAndDeleteById_thenDelete() {
        Income income = incomes.get(2);
        income.setId(15L);

        when(repository.existsById(any(Long.class))).thenReturn(Boolean.TRUE);
        doNothing().when(repository).deleteById(any(Long.class));
        service.delete("15");

        assertDoesNotThrow(() -> {});
    }

    @Test
    @DisplayName("Should throw exception when income does not exist")
    void whenNoExistsIncome_thenThrowException() {
        String id = "10";
        when(repository.existsById(any(Long.class))).thenReturn(Boolean.FALSE);
        final NoSuchElementException validationException =
                assertThrows(NoSuchElementException.class, () -> service.delete(id));

        assertEquals("Income not exist by ID " + id, validationException.getMessage());
    }

    @Test
    @DisplayName("Should update income")
    void whenUpdateValidIncome_thenOk() {
        Income income = incomes.get(3);
        income.setId(3L);
        incomeRequest = IncomeRequest.builder()
                .date("18/11/2022")
                .value("5100.00")
                .description("iPhone 12 Pro")
                .build();

        Income updatedVisaCard = new Income(incomeRequest);

        when(repository.findByDescription(any(String.class))).thenReturn(Optional.empty());
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(income));
        when(repository.saveAndFlush(any(Income.class))).thenReturn(updatedVisaCard);

        IncomeResponse response = service.update("3", incomeRequest);

        assertAll(
                () -> assertDoesNotThrow(() -> {}),
                () -> assertEquals(incomeRequest.getDescription(), response.getDescription()));
    }

    @Test
    @DisplayName("Should not update income")
    void whenUpdateIncomeWithDescriptionInTheSameMonth_thenThrowException() {
        Income income = incomes.get(2);
        income.setId(3L);
        incomeRequest = IncomeRequest.builder()
                .date("28/10/2022")
                .value("100.00")
                .description("Loan")
                .build();
        when(repository.findByDescription(any(String.class))).thenReturn(Optional.of(income));
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(income));
        final ValidationException validationException = assertThrows(ValidationException.class,
                () -> service.update("3", incomeRequest));

        assertEquals(
                "Income cannot be updated, was registered in the current month",
                validationException.getMessage());
    }

    @Test
    @DisplayName("Should delete income by id")
    void whenDeleteById_thenThrowsException() {
        when(repository.existsById(32L)).thenReturn(false);
        doNothing().when(repository).deleteById(any(Long.class));

        final NoSuchElementException validationException =
                assertThrows(NoSuchElementException.class, () -> service.delete("32"));

        assertEquals("Income not exist by ID 32", validationException.getMessage());
    }

    @Test
    @DisplayName("Should return incomes from the same month")
    void whenFindByMonth_thenReturnIncomesForTheMonthInformed() {
        String month = "11";
        String year = "2022";
        List<Income> matchers = incomes.stream()
                .filter(income ->
                        Objects.equals(income.getDate().getMonth().getValue(), Month.NOVEMBER.getValue())
                        && Objects.equals(income.getDate().getYear(), Year.of(2022).getValue()))
                .collect(Collectors.toList());

        when(repository.findByMonth(11, 2022)).thenReturn(matchers);
        final List<IncomeResponse> incomeResponseList = service.findByMonth(month, year);
        assertEquals(3, incomeResponseList.size());
    }
}