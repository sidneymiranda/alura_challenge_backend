package br.com.sidney.alura_challenge_backend.service;

import br.com.sidney.alura_challenge_backend.dto.IncomeRequest;
import br.com.sidney.alura_challenge_backend.dto.IncomeResponse;
import br.com.sidney.alura_challenge_backend.model.Income;
import br.com.sidney.alura_challenge_backend.repository.IncomeRepository;
import br.com.sidney.alura_challenge_backend.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class IncomeService {
    private final IncomeRepository repository;

    @Autowired
    public IncomeService(IncomeRepository repository) {
        this.repository = repository;
    }

    public IncomeResponse register(IncomeRequest request) {
        if(exists(request.getDescription(), request.getDate())) {
            throw new ValidationException("The income already registered for the informed month");
        }

        Income income = new Income(request);

        income = this.repository.save(income);

        return new IncomeResponse(income);
    }

    private boolean exists(String description, String date) {
        Optional<Income> income = this.repository.findByDescription(description);

        if(income.isEmpty()) {
            return false;
        }

        LocalDateTime register = income.get().getDate();

        return register.getMonth().equals(DateUtils.stringToDate(date).getMonth());
    }

}
