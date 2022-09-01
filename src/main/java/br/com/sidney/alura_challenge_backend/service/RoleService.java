package br.com.sidney.alura_challenge_backend.service;

import br.com.sidney.alura_challenge_backend.dto.RoleRequest;
import br.com.sidney.alura_challenge_backend.dto.RoleResponse;
import br.com.sidney.alura_challenge_backend.model.Role;
import br.com.sidney.alura_challenge_backend.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final AuthorityRepository repository;

    public RoleResponse save(RoleRequest role) {
        Role newRole = repository.save(new Role(role));
        return new RoleResponse(newRole);
    }
}
