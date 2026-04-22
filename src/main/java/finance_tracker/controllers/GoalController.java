package finance_tracker.controllers;

import finance_tracker.dtos.goal.AddFundsRequest;
import finance_tracker.dtos.goal.GoalRequest;
import finance_tracker.dtos.goal.GoalResponse;
import finance_tracker.services.GoalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@CrossOrigin(origins = "*") // Recuerda ajustar esto a la URL de tu Angular
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping
    public ResponseEntity<List<GoalResponse>> getMyGoals() {
        // Sacamos el email del JWT
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(goalService.getGoalsByUser(email));
    }

    @PostMapping
    public ResponseEntity<GoalResponse> createGoal(@RequestBody GoalRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(goalService.createGoal(request, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build(); // Devuelve un código 204 (Todo ok, sin contenido)
    }

    @PostMapping("/{id}/add-funds")
    public ResponseEntity<GoalResponse> addFunds(@PathVariable Long id, @RequestBody AddFundsRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(goalService.addFundsToGoal(id, request, email));
    }
}
