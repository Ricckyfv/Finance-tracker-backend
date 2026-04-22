package finance_tracker.dtos.goal;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddFundsRequest {
    private BigDecimal amount;
}
