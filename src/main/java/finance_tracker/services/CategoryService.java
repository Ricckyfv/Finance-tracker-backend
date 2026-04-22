package finance_tracker.services;

import finance_tracker.dtos.category.CategoryResponseDTO;
import finance_tracker.models.Category;
import finance_tracker.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 2. OBTENER MIS CATEGORÍAS
    public List<CategoryResponseDTO> getAllCategories() {

        // Buscamos solo las categorías de este usuario
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Método privado para mapear manualmente
    private CategoryResponseDTO mapToResponseDTO(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setType(category.getType());
        return dto;
    }
}
