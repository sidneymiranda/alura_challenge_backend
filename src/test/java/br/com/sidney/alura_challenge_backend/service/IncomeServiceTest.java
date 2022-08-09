package br.com.sidney.alura_challenge_backend.service;

import br.com.sidney.alura_challenge_backend.dto.IncomeRequest;
import br.com.sidney.alura_challenge_backend.dto.IncomeResponse;
import br.com.sidney.alura_challenge_backend.model.Income;
import br.com.sidney.alura_challenge_backend.repository.IncomeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.ConstraintValidator;
import javax.validation.ValidationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IncomeServiceTest {

    private static IncomeService service;

    private static IncomeRepository repository;

    @BeforeAll
    public static void init() {
        repository = mock(IncomeRepository.class);
        service = new IncomeService(repository);
    }

    @Test
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
    void thenGetAll_thenReturnList() {
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

        assertTrue(incomeResponseList.size() == 3);
    }
}