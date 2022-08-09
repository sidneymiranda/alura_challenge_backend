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
}