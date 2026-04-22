package finance_tracker.dtos.goal;

import finance_tracker.models.GoalCategory;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data // Usa Lombok para generar Getters/Setters automáticamente
public class GoalRequest {
    // Fíjate que NO pedimos ni id, ni savedAmount, ni User. Esos los controlamos nosotros.
//    private String name;
    private GoalCategory goalCategory;
    private String description;
    private BigDecimal targetAmount;
    private LocalDate deadline;
}