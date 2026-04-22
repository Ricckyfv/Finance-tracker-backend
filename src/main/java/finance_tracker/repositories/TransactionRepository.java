package finance_tracker.repositories;

import finance_tracker.models.CategoryType;
import finance_tracker.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Trae las transacciones de un usuario ordenadas por fecha descendente
    List<Transaction> findByUserIdOrderByTransactionDateDesc(Long userId);

    // Consulta personalizada para sumar el dinero según el tipo (INCOME o EXPENSE)
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.category.type = :type")
    BigDecimal sumAmountByUserIdAndCategoryType(@Param("userId") Long userId, @Param("type") CategoryType type);

    // Busca plantillas recurrentes que se crearon en un día específico del mes
    @Query("SELECT t FROM Transaction t WHERE t.recurring = true AND EXTRACT(DAY FROM t.transactionDate) = :day")
    List<Transaction> findRecurringTransactionsByDay(@Param("day") int day);

    // Verifica si ya existe un cobro exacto hoy para no duplicarlo
    boolean existsByUserIdAndDescriptionAndTransactionDate(Long userId, String description, LocalDate transactionDate);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.category.id = :categoryId AND EXTRACT(MONTH FROM t.transactionDate) = :month AND EXTRACT(YEAR FROM t.transactionDate) = :year")
    BigDecimal sumAmountByUserIdAndCategoryIdAndMonthAndYear(@Param("userId") Long userId, @Param("categoryId") Long categoryId, @Param("month") int month, @Param("year") int year);
}
