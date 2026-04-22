package finance_tracker.dtos.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequestDTO {

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private BigDecimal amount;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate transactionDate; // Formato esperado en JSON: "YYYY-MM-DD"

    @NotNull(message = "Debes especificar el ID de la categoría")
    private Long categoryId;

    // Para nuestra Idea A (Suscripciones)
    private boolean recurring;
}
