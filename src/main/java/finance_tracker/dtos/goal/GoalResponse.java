package finance_tracker.dtos.goal;

import finance_tracker.models.GoalCategory;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GoalResponse {
    private Long id;
//    private String name;
    private GoalCategory goalCategory;
    private String description;
    private BigDecimal targetAmount;
    private BigDecimal savedAmount;
    private LocalDate deadline;
    // Ocultamos el User por seguridad y limpieza
}
