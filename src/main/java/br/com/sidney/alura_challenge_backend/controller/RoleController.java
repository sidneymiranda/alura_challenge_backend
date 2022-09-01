package br.com.sidney.alura_challenge_backend.controller;

import br.com.sidney.alura_challenge_backend.dto.RoleRequest;
import br.com.sidney.alura_challenge_backend.dto.RoleResponse;
import br.com.sidney.alura_challenge_backend.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    @Autowired
    private final RoleService service;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleResponse> resumeMonth(@RequestBody RoleRequest role) {
        return ResponseEntity.ok().body(this.service.save(role));
    }
}
