package br.com.sidney.alura_challenge_backend.repository;

import br.com.sidney.alura_challenge_backend.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    Optional<Income> findByDescription(String description);
}
