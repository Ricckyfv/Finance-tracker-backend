package finance_tracker.dtos.category;

import finance_tracker.models.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequestDTO {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String name;

    @NotNull(message = "Debes especificar si es INCOME (Ingreso) o EXPENSE (Gasto)")
    private CategoryType type;
}
