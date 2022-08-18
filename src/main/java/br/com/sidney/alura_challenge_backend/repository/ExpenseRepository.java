package br.com.sidney.alura_challenge_backend.repository;

import br.com.sidney.alura_challenge_backend.model.Expense;
import br.com.sidney.alura_challenge_backend.dto.CategoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<Expense> findByDescription(String description);

    List<Expense> findByDescriptionContainingIgnoreCase(String description);

    @Query(value = "SELECT e.id, e.description, e.date, e.value, e.category FROM expense e " +
            "WHERE extract(year from e.date) = ?1 AND extract(month from e.date) = ?2",
            nativeQuery = true)
    List<Expense> findByMonth(int year, int month);

    @Query(value = "SELECT sum(e.value) FROM expense e WHERE extract(year from e.date) = ?1 " +
            "AND extract(month from e.date) = ?2",
            nativeQuery = true)
    BigDecimal amountByMonth(int year, int month);

    @Query(value = "SELECT NEW br.com.sidney.alura_challenge_backend.vo.CategoryVO(e.category, SUM(e.value)) " +
            "FROM Expense e WHERE YEAR(e.date) = ?1 AND MONTH(e.date) = ?2 GROUP BY e.category")
    List<CategoryResponse> amountByCategory(int year, int month);
}
