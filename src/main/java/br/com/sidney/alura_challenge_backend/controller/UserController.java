package br.com.sidney.alura_challenge_backend.controller;

import br.com.sidney.alura_challenge_backend.dto.UserRequest;
import br.com.sidney.alura_challenge_backend.dto.UserResponse;
import br.com.sidney.alura_challenge_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Validated
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> resumeMonth(@RequestBody UserRequest user) {
        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/api/v1/{username}")
                .buildAndExpand(user.getUsername())
                .toUri();
        return ResponseEntity.created(uri).body(this.service.save(user));
    }
}
