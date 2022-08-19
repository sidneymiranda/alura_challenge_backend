package br.com.sidney.alura_challenge_backend.controller;

import br.com.sidney.alura_challenge_backend.dto.IncomeRequest;
import br.com.sidney.alura_challenge_backend.dto.IncomeResponse;
import br.com.sidney.alura_challenge_backend.service.IncomeService;
import br.com.sidney.alura_challenge_backend.validators.interfaces.Month;
import br.com.sidney.alura_challenge_backend.validators.interfaces.Year;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/v1/incomes")
public class IncomeController {

    private final IncomeService service;

    @Autowired
    public IncomeController(IncomeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<IncomeResponse> register(@Valid @RequestBody IncomeRequest income,
                                                   UriComponentsBuilder uriBuilder) {
       IncomeResponse incomeResponse = this.service.register(income);
       URI address = uriBuilder.path("/incomes/{id}").buildAndExpand(incomeResponse.getId()).toUri();
       return ResponseEntity.created(address).body(incomeResponse);
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponse>> getAll(@RequestParam(required = false) Optional<String> description) {
        return ResponseEntity.ok().body(this.service.getAll(description));
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<IncomeResponse>> findByMonth(
            @PathVariable @Year Integer year,
            @PathVariable @Month Integer month) {
        return ResponseEntity.ok().body(this.service.findByMonth(year, month));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        final Optional<IncomeResponse> income = this.service.findById(id);
        return income.isPresent()
                ? ResponseEntity.ok(this.service.findById(id))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<IncomeResponse> update(@PathVariable String id, @RequestBody IncomeRequest income) {
        return ResponseEntity.ok().body(this.service.update(id, income));
    }
}
