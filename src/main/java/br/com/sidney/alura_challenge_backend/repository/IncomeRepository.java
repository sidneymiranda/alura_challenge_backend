package br.com.sidney.alura_challenge_backend.repository;

import br.com.sidney.alura_challenge_backend.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    Optional<Income> findByDescription(String description);

    List<Income> findByDescriptionContainingIgnoreCase(String description);

    @Query(value = "SELECT * FROM income i WHERE extract(year from i.date) = ?1 AND extract(month from i.date) = ?2",
            nativeQuery = true)
    List<Income> findByMonth(Integer year, Integer month);

    @Query(value = "SELECT sum(i.value) FROM income i WHERE extract(year from i.date) = ?1 AND extract(month from i.date) = ?2",
    nativeQuery = true)
    BigDecimal amountByMonth(Integer year, Integer month);
}
