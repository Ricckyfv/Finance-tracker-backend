package finance_tracker.dtos.category;

import finance_tracker.models.CategoryType;
import lombok.Data;

@Data
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private CategoryType type;
}
