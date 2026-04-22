package finance_tracker.dtos.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor // Crea un constructor con los 3 parámetros
public class TransactionSummaryDTO {
    private BigDecimal totalIncome;   // Total de ingresos
    private BigDecimal totalExpenses; // Total de gastos
    private BigDecimal balance;       // Saldo final (Ingresos - Gastos)
}
