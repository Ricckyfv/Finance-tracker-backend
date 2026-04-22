package finance_tracker.services;

import finance_tracker.dtos.goal.AddFundsRequest;
import finance_tracker.dtos.goal.GoalRequest;
import finance_tracker.dtos.goal.GoalResponse;
import finance_tracker.models.Category;
import finance_tracker.models.Goal;
import finance_tracker.models.Transaction;
import finance_tracker.models.User;
import finance_tracker.repositories.CategoryRepository;
import finance_tracker.repositories.GoalRepository;
import finance_tracker.repositories.TransactionRepository;
import finance_tracker.repositories.UserRepository;
import finance_tracker.mappers.GoalMapper; // Importamos el Mapper
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final GoalMapper goalMapper;

    public GoalService(GoalRepository goalRepository, UserRepository userRepository, TransactionRepository transactionRepository, CategoryRepository categoryRepository, GoalMapper goalMapper) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.goalMapper = goalMapper;
    }

    public List<GoalResponse> getGoalsByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return goalRepository.findByUserId(user.getId())
                .stream()
                .map(goalMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public GoalResponse createGoal(GoalRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 1. DTO a Entidad
        Goal goal = goalMapper.toEntity(request);

        goal.setUser(user);

        // 2. Guardamos en BD
        Goal savedGoal = goalRepository.save(goal);

        // 3. Entidad guardada a DTO
        return goalMapper.toResponseDTO(savedGoal);
    }

    @Transactional
    public void deleteGoal(Long goalId) {
        goalRepository.deleteById(goalId);
    }

    @Transactional
    public GoalResponse addFundsToGoal(Long goalId, AddFundsRequest request, String email) {
        // 1. Verificamos que el usuario exista
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Buscamos la meta y verificamos que le pertenezca a este usuario
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));

        if (!goal.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tienes permiso para modificar esta meta");
        }

        // 3. Sumamos el dinero al ahorro actual
        BigDecimal newSavedAmount = goal.getSavedAmount().add(request.getAmount());
        goal.setSavedAmount(newSavedAmount);
        Goal savedGoal = goalRepository.save(goal);

        Transaction savingTransaction = new Transaction();
        savingTransaction.setUser(user);
        savingTransaction.setAmount(request.getAmount());
        savingTransaction.setTransactionDate(LocalDate.now());
        savingTransaction.setRecurring(false);

        String customDesc = "";
        if (goal.getDescription() != null && !goal.getDescription().trim().isEmpty()) {
            customDesc = " (" + goal.getDescription() + ")";
        }

        // Guardamos el texto final de la descripción, incluyendo el nombre de la categoría y la descripción personalizada si existe
        savingTransaction.setDescription("Abono a meta: " + goal.getGoalCategory().name() + customDesc);

        Category savingCategory = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equalsIgnoreCase("Ahorros"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Error crítico: La categoría 'Ahorros' no existe en la base de datos."));

        savingTransaction.setCategory(savingCategory);
        savingTransaction.setGoal(goal);

        // 3. Guardamos la transacción en el historial
        transactionRepository.save(savingTransaction);

        return goalMapper.toResponseDTO(savedGoal);
    }
}
