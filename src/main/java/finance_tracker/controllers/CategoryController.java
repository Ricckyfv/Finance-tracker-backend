package finance_tracker.controllers;

import finance_tracker.dtos.category.CategoryRequestDTO;
import finance_tracker.dtos.category.CategoryResponseDTO;
import finance_tracker.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // POST /api/categories
//    @PostMapping
//    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {
//        return ResponseEntity.ok(categoryService.createCategory(requestDTO));
//    }

    // GET /api/categories
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
