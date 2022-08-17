package br.com.sidney.alura_challenge_backend.controller;

import br.com.sidney.alura_challenge_backend.dto.ExpenseRequest;
import br.com.sidney.alura_challenge_backend.dto.ExpenseResponse;
import br.com.sidney.alura_challenge_backend.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
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
                ? ResponseEntity.ok(this.service.findById(id))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<ExpenseResponse>> findByMonth(@PathVariable String year, @PathVariable String month) {
        return ResponseEntity.ok().body(this.service.findByMonth(month, year));
    }

    @PutMapping("{id}")
    public ResponseEntity<ExpenseResponse> update(@PathVariable String id, @RequestBody ExpenseRequest expense) {
        return ResponseEntity.ok().body(this.service.update(id, expense));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }
}




