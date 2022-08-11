package br.com.sidney.alura_challenge_backend.service;

import br.com.sidney.alura_challenge_backend.dto.IncomeRequest;
import br.com.sidney.alura_challenge_backend.dto.IncomeResponse;
import br.com.sidney.alura_challenge_backend.model.Income;
import br.com.sidney.alura_challenge_backend.repository.IncomeRepository;
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

@DisplayName("Writing assertions for income services")
class IncomeServiceTest {

    private static IncomeService service;

    private static IncomeRepository repository;

    @BeforeAll
    public static void init() {
        repository = mock(IncomeRepository.class);
        service = new IncomeService(repository);
    }

    @Test
    @DisplayName("Should save income")
    void whenRegister_thenSave() {
        IncomeRequest request = new IncomeRequest();
        request.setDate("08/08/2022 18:00");
        request.setDescription("Curso Design Pattern");
        request.setValue("99.90");

        Income income = new Income(request);
        when(repository.save(any(Income.class))).thenReturn(income);

        IncomeResponse response = service.register(request);

        assertNotNull(response);
    }

    @Test
    @DisplayName("Should not save income that containing the same description within the same month")
    void whenIncomeWithDescriptionAndSameMonth_thenNotSave() {
        IncomeRequest request = new IncomeRequest();
        request.setDate("08/08/2022 18:00");
        request.setDescription("Curso Design Pattern");
        request.setValue("99.90");

        Income income = new Income(request);

        when(repository.findByDescription(any(String.class))).thenReturn(Optional.of(income));

        final ValidationException validationException = assertThrows(ValidationException.class, () -> service.register(request));

        assertEquals("The income already registered for the informed month", validationException.getMessage());
    }

    @Test
    @DisplayName("Should return all saved incomes")
    void thenGetAll_thenReturnAllIncomes() {
        IncomeRequest curse = new IncomeRequest();
        curse.setDate("08/08/2022 18:00");
        curse.setDescription("Curso Design Pattern");
        curse.setValue("99.90");

        IncomeRequest internet = new IncomeRequest();
        internet.setDate("10/08/2022 11:00");
        internet.setDescription("Conta de internet");
        internet.setValue("199.90");

        IncomeRequest creditCard = new IncomeRequest();
        creditCard.setDate("08/08/2022 18:00");
        creditCard.setDescription("Visa Credit Card");
        creditCard.setValue("2099.90");

        final List<Income> incomes = Arrays.asList(new Income(curse), new Income(internet), new Income(creditCard));
        when(repository.findAll()).thenReturn(incomes);

        final List<IncomeResponse> incomeResponseList = service.getAll();

        assertEquals(3, incomeResponseList.size());
    }

    @Test
    @DisplayName("Should return income through id")
    void whenIncomeFindById_thenReturnOneIncome() {
        IncomeRequest internet = new IncomeRequest();
        internet.setDate("10/08/2022 11:00");
        internet.setDescription("Conta de internet");
        internet.setValue("199.90");

        Income income = new Income(internet);
        income.setId(1L);

        when(repository.existsById(any(Long.class))).thenReturn(Boolean.TRUE);
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(income));

        final Optional<IncomeResponse> response = service.findById("1");

        assertEquals(income.getId().toString(), response.get().getId());
    }

    @Test
    @DisplayName("Should delete income through id")
    void whenExistsIncomeAndDeleteById_thenDeleteFromDb() {
        IncomeRequest creditCard = new IncomeRequest();
        creditCard.setDate("08/08/2022 18:00");
        creditCard.setDescription("Visa Credit Card");
        creditCard.setValue("2099.90");

        Income income = new Income(creditCard);
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
        final NoSuchElementException validationException = assertThrows(NoSuchElementException.class, () -> service.delete("10"));

        assertEquals("Income not exist by ID 10", validationException.getMessage());
    }

    @Test
    @DisplayName("Should update income")
    void whenUpdateValidIncome_thenOk() {
        Income income = new Income();
        income.setId(3L);
        income.setDescription("Visa card");
        income.setDate(DateUtils.stringToDate("08/08/2022 18:00"));
        income.setValue(new BigDecimal("2500.00"));

        Income updatedVisaCard = new Income();
        updatedVisaCard.setValue(new BigDecimal("3500.00"));
        updatedVisaCard.setDescription("Visa card 2");
        updatedVisaCard.setDate(DateUtils.stringToDate("08/08/2022 18:00"));

        when(repository.findByDescription(any(String.class))).thenReturn(Optional.empty());
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(income));
        when(repository.saveAndFlush(any(Income.class))).thenReturn(updatedVisaCard);

        IncomeRequest request = new IncomeRequest();
        request.setDescription("Visa card 2");
        request.setValue("3500.00");
        request.setDate("18/08/2022 18:00");

        IncomeResponse response = service.update("3", request);

        assertAll(
                () -> assertDoesNotThrow(() -> {
                }),
                () -> assertEquals(request.getValue(), response.getValue()),
                () -> assertNotEquals(income.getValue(), new BigDecimal(response.getValue()))
        );
    }

    @Test
    @DisplayName("Should not update income")
    void whenUpdateIncomeWithDescriptionInTheSameMonth_thenThrowException() {
        Income income = new Income();
        income.setId(3L);
        income.setDescription("Visa card");
        income.setDate(DateUtils.stringToDate("08/08/2022 18:00"));
        income.setValue(new BigDecimal("2500.00"));

        when(repository.findByDescription(any(String.class))).thenReturn(Optional.of(income));

        IncomeRequest request = new IncomeRequest();
        request.setDescription("Visa card");
        request.setValue("3500.00");
        request.setDate("18/08/2022 18:00");

        final ValidationException validationException = assertThrows(ValidationException.class, () -> service.update("3", request));
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