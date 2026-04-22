package finance_tracker.dtos.transaction;

import finance_tracker.models.CategoryType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionResponseDTO {
    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;
    private boolean recurring;

    // Datos planos de la categoría (Flattening)
    private Long categoryId;
    private String categoryName;
    private CategoryType categoryType;
    private boolean budgetExceeded; // Nuevo: indica si este gasto rompió el presupuesto
}
