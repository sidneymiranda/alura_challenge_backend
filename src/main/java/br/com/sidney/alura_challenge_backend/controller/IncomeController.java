package br.com.sidney.alura_challenge_backend.controller;

import br.com.sidney.alura_challenge_backend.dto.IncomeRequest;
import br.com.sidney.alura_challenge_backend.dto.IncomeResponse;
import br.com.sidney.alura_challenge_backend.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
}
