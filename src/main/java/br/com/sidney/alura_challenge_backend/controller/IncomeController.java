package br.com.sidney.alura_challenge_backend.controller;

import br.com.sidney.alura_challenge_backend.dto.IncomeRequest;
import br.com.sidney.alura_challenge_backend.dto.IncomeResponse;
import br.com.sidney.alura_challenge_backend.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/incomes")
public class IncomeController {

    private final IncomeService service;

    @Autowired
    public IncomeController(IncomeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<IncomeResponse> register(@Valid @RequestBody IncomeRequest income) throws URISyntaxException {
       IncomeResponse incomeResponse = this.service.register(income);

       return ResponseEntity.created(new URI("")).body(incomeResponse);
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponse>> getAll() {
        return ResponseEntity.ok().body(this.service.getAll());
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
}
