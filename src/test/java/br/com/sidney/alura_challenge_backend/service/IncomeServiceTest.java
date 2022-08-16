package br.com.sidney.alura_challenge_backend.service;

import br.com.sidney.alura_challenge_backend.dto.IncomeRequest;
import br.com.sidney.alura_challenge_backend.dto.IncomeResponse;
import br.com.sidney.alura_challenge_backend.model.Income;
import br.com.sidney.alura_challenge_backend.repository.IncomeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
                .value("99.90")
                .description("Curso Design Pattern")
                .build();
        incomes.add(new Income(incomeRequest));

        incomeRequest = IncomeRequest.builder()
                .date("10/08/2022")
                .value("199.90")
                .description("Internet Account")
                .build();
        incomes.add(new Income(incomeRequest));

        incomeRequest = IncomeRequest.builder()
                .date("08/08/2022")
                .value("2099.90")
                .description("Visa Credit Card")
                .build();
        incomes.add(new Income(incomeRequest));
    }

    @Test
    @DisplayName("Should save income")
    void whenRegister_thenSave() {
        incomeRequest = IncomeRequest.builder()
                .date("01/01/2022")
                .value("3099.90")
                .description("Nubank Card")
                .build();
        Income income = new Income(incomeRequest);
        when(repository.save(any(Income.class))).thenReturn(income);

        IncomeResponse response = service.register(incomeRequest);

        assertNotNull(response);
    }

    @Test
    @DisplayName("Should not save income that containing the same description within the same month")
    void whenIncomeWithDescriptionAndSameMonth_thenNotSave() {
        incomeRequest = IncomeRequest.builder()
                .date("15/01/2022")
                .value("3099.90")
                .description("MasterCard")
                .build();

        Income income = new Income(incomeRequest);

        when(repository.findByDescription(any(String.class))).thenReturn(Optional.of(income));

        final ValidationException validationException = assertThrows(ValidationException.class, () -> service.register(incomeRequest));

        assertEquals("The income already registered for the informed month", validationException.getMessage());
    }

    @Test
    @DisplayName("Should return all saved incomes")
    void thenGetAll_thenReturnAllIncomes() {
        when(repository.findAll()).thenReturn(incomes);
        final List<IncomeResponse> incomeResponseList = service.getAll();
        assertEquals(3, incomeResponseList.size());
    }

    @Test
    @DisplayName("Should return income through id")
    void whenIncomeFindById_thenReturnOneIncome() {
        Income income = new Income(incomeRequest);
        income.setId(1L);

        when(repository.existsById(any(Long.class))).thenReturn(Boolean.TRUE);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(income));

        final Optional<IncomeResponse> response = service.findById("1");

        assertEquals(income.getId().toString(), response.get().getId());
    }

    @Test
    @DisplayName("Should delete income through id")
    void whenExistsIncomeAndDeleteById_thenDeleteFromDb() {
        Income income = new Income(incomeRequest);
        income.setId(15L);

        when(repository.existsById(any(Long.class))).thenReturn(Boolean.TRUE);
        doNothing().when(repository).deleteById(any(Long.class));
        service.delete("15");

        assertDoesNotThrow(() -> {});
    }

    @Test
    @DisplayName("Should throw exception when income does not exist")
    void whenNoExistsIncome_thenThrowException() {
        when(repository.existsById(any(Long.class))).thenReturn(Boolean.FALSE);
        final NoSuchElementException validationException =
                assertThrows(NoSuchElementException.class, () -> service.delete("10"));

        assertEquals("Income not exist by ID 10", validationException.getMessage());
    }

    @Test
    @DisplayName("Should update income")
    void whenUpdateValidIncome_thenOk() {
        Income income = incomes.get(2);
        incomeRequest = IncomeRequest.builder()
                .date("08/08/2022")
                .value("2099.90")
                .description("Nubank")
                .build();

        Income updatedVisaCard = new Income(incomeRequest);

        when(repository.findByDescription(any(String.class))).thenReturn(Optional.empty());
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(income));
        when(repository.saveAndFlush(any(Income.class))).thenReturn(updatedVisaCard);

        IncomeResponse response = service.update("3", incomeRequest);

        assertAll(
                () -> assertDoesNotThrow(() -> {}),
                () -> assertEquals(incomeRequest.getValue(), response.getValue()));
    }

    @Test
    @DisplayName("Should not update income")
    void whenUpdateIncomeWithDescriptionInTheSameMonth_thenThrowException() {
        Income income = incomes.get(2);
        incomeRequest = IncomeRequest.builder()
                .date("08/08/2022")
                .description("Visa Credit Card")
                .value("9000.00")
                .build();
        when(repository.findByDescription(any(String.class))).thenReturn(Optional.of(income));
        final ValidationException validationException = assertThrows(ValidationException.class, () -> service.update("2", incomeRequest));

        assertEquals("Income cannot be updated, was registered in the current month", validationException.getMessage());
    }

    @Test
    @DisplayName("Should delete income by id")
    void whenDeleteById_thenThrowsException() {
        when(repository.existsById(32L)).thenReturn(false);
        doNothing().when(repository).deleteById(any(Long.class));

        final NoSuchElementException validationException = assertThrows(NoSuchElementException.class, () -> service.delete("32"));

        assertEquals("Income not exist by ID 32", validationException.getMessage());
    }
}