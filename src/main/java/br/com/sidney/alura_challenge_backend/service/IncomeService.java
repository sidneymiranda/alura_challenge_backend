package br.com.sidney.alura_challenge_backend.service;

import br.com.sidney.alura_challenge_backend.dto.IncomeRequest;
import br.com.sidney.alura_challenge_backend.dto.IncomeResponse;
import br.com.sidney.alura_challenge_backend.model.Income;
import br.com.sidney.alura_challenge_backend.repository.IncomeRepository;
import br.com.sidney.alura_challenge_backend.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IncomeService {
    private final IncomeRepository repository;

    @Autowired
    public IncomeService(IncomeRepository repository) {
        this.repository = repository;
    }

    public IncomeResponse register(IncomeRequest request) {
        if(validateRecordDate(request.getDescription(), request.getDate())) {
            throw new ValidationException("The income already registered for the informed month");
        }

        Income income = new Income(request);

        income = this.repository.save(income);

        return new IncomeResponse(income);
    }

    public List<IncomeResponse> getAll(Optional<String> description) {
        List<Income> incomes;
        if(description.isEmpty())
            incomes = this.repository.findAll();
        else
            incomes = this.repository.findByDescriptionContainingIgnoreCase(description.get());

        return incomes.stream().map(IncomeResponse::new)
                .collect(Collectors.toList());
    }

    public List<IncomeResponse> findByMonth(Integer year, Integer month) {
        return this.repository.findByMonth(year, month)
                .stream()
                .map(IncomeResponse::new)
                .collect(Collectors.toList());
    }

    public Optional<IncomeResponse> findById(String id) {
        return this.repository.findById(Long.parseLong(id)).map(IncomeResponse::new);
    }

    public void delete(String id) {
        if (!isPresent(id))
            throw new NoSuchElementException("Income not exist by ID " + id);

        this.repository.deleteById(Long.parseLong(id));
    }

    private boolean isPresent(String id) {
        return this.repository.existsById(Long.parseLong(id));
    }

    private boolean validateRecordDate(String description, String date) {
        Optional<Income> income = findByDescription(description);

        if(income.isEmpty())
            return false;

        LocalDate register = income.get().getDate();

        return register.getYear() == DateUtils.stringToDate(date).getYear()
                && register.getMonth().equals(DateUtils.stringToDate(date).getMonth());
    }

    public Optional<Income> findByDescription(String description) {
        return this.repository.findByDescription(description);
    }

    public IncomeResponse update(String id, IncomeRequest incomeRequest) {
        if (validateRecordDate(incomeRequest.getDescription(), incomeRequest.getDate()))
            throw new ValidationException("Income cannot be updated, was registered in the current month");

        Income income = this.repository.findById(Long.parseLong(id))
                .orElseThrow(() -> new NoSuchElementException(""));

        BeanUtils.copyProperties(incomeRequest, income);

        return new IncomeResponse(this.repository.saveAndFlush(income));
    }

}
