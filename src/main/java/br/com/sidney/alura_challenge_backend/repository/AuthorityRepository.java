package br.com.sidney.alura_challenge_backend.repository;

import br.com.sidney.alura_challenge_backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
