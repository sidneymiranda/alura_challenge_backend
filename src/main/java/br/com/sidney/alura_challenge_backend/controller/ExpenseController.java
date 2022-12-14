package br.com.sidney.alura_challenge_backend.controller;

import br.com.sidney.alura_challenge_backend.dto.ExpenseRequest;
import br.com.sidney.alura_challenge_backend.dto.ExpenseResponse;
import br.com.sidney.alura_challenge_backend.service.ExpenseService;
import br.com.sidney.alura_challenge_backend.validators.interfaces.Month;
import br.com.sidney.alura_challenge_backend.validators.interfaces.Year;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService service;

    @Autowired
    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> register(@Valid @RequestBody ExpenseRequest income,
                                                    UriComponentsBuilder uriBuilder) {
       ExpenseResponse expenseResponse = this.service.register(income);
        URI address = uriBuilder.path("/expenses/{id}").buildAndExpand(expenseResponse.getId()).toUri();
       return ResponseEntity.created(address).body(expenseResponse);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAll(@RequestParam(required = false) Optional<String> description) {
        return ResponseEntity.ok().body(this.service.getAll(description));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        final Optional<ExpenseResponse> expense = this.service.findById(id);
        return expense.isPresent()
                ? ResponseEntity.ok(expense)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<ExpenseResponse>> findByMonth(
            @PathVariable @Year Integer year,
            @PathVariable @Month Integer month) {
        return ResponseEntity.ok().body(this.service.findByMonth(year, month));
    }

    @PutMapping("{id}")
    public ResponseEntity<ExpenseResponse> update(
            @PathVariable String id,
            @RequestBody ExpenseRequest expense) {
        return ResponseEntity.ok().body(this.service.update(id, expense));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }
}




