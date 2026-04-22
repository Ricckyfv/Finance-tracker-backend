package finance_tracker.controllers;

import finance_tracker.models.Budget;
import finance_tracker.models.Category;
import finance_tracker.repositories.BudgetRepository;
import finance_tracker.repositories.CategoryRepository;
import finance_tracker.services.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final CurrentUserService currentUserService;

    public BudgetController(BudgetRepository budgetRepository,
                            CategoryRepository categoryRepository,
                            CurrentUserService currentUserService) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.currentUserService = currentUserService;
    }

    // OBTENER PRESUPUESTOS
    @GetMapping
    public ResponseEntity<List<Budget>> getMyBudgets() {
        return ResponseEntity.ok(budgetRepository.findByUserId(currentUserService.get().getId()));
    }

    // CREAR PRESUPUESTO
    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody BudgetRequestDTO request) {
        // 1. Buscamos la categoría real en la base de datos
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // 2. Construimos el presupuesto
        Budget newBudget = new Budget();
        newBudget.setAmountLimit(request.amountLimit());
        newBudget.setMonth(request.month());
        newBudget.setYear(request.year());
        newBudget.setCategory(category);
        newBudget.setUser(currentUserService.get());

        // 3. Guardamos
        return ResponseEntity.ok(budgetRepository.save(newBudget));
    }

    // BORRAR PRESUPUESTO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Nuestro DTO interno para recibir los datos exactos de Angular
    public record BudgetRequestDTO(Long categoryId, BigDecimal amountLimit, int month, int year) {}
}