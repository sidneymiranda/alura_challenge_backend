package br.com.sidney.alura_challenge_backend.repository;

import br.com.sidney.alura_challenge_backend.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<Expense> findByDescription(String description);

    List<Expense> findByDescriptionContainingIgnoreCase(String description);

    @Query(value = "SELECT * FROM income i WHERE extract(month from i.date) = ?1 AND extract(year from i.date) = ?2",
            nativeQuery = true)
    List<Expense> findByMonth(int month, int year);
}
