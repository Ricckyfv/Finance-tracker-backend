package finance_tracker.services;

import finance_tracker.dtos.transaction.TransactionRequestDTO;
import finance_tracker.dtos.transaction.TransactionResponseDTO;
import finance_tracker.dtos.transaction.TransactionSummaryDTO;
import finance_tracker.mappers.TransactionMapper;
import finance_tracker.models.*;
import finance_tracker.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final GoalRepository goalRepository;
    private final CurrentUserService currentUserService;
    private final BudgetService budgetService;
    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository,
                              CategoryRepository categoryRepository, GoalRepository goalRepository,
                              CurrentUserService currentUserService, BudgetService budgetService, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.goalRepository = goalRepository;
        this.currentUserService = currentUserService;
        this.budgetService = budgetService;
        this.transactionMapper = transactionMapper;
    }

    // 1. CREAR TRANSACCIÓN
    public TransactionResponseDTO createTransaction(TransactionRequestDTO requestDTO) {
        User user = currentUserService.get();

        // Buscamos la categoría en la BD
        Category category = getCategory(requestDTO.getCategoryId());

        // --- LÓGICA DE ALERTA DE PRESUPUESTO ---
        boolean exceeded = budgetService.isBudgetExceeded(user, category, requestDTO.getAmount());

        // Construimos la transacción
        Transaction transaction = new Transaction();
        transactionMapper.updateEntity(transaction, requestDTO); // Usamos el mapper para copiar los campos comunes
        transaction.setCategory(category);
        transaction.setUser(user);

        Transaction savedTransaction = transactionRepository.save(transaction);


        TransactionResponseDTO response = transactionMapper.toResponseDTO(savedTransaction);
        response.setBudgetExceeded(exceeded);

        return response;
    }

    // 2. OBTENER MIS TRANSACCIONES
    public List<TransactionResponseDTO> getMyTransactions() {

        return transactionRepository.findByUserIdOrderByTransactionDateDesc(currentUserService.get().getId())
                .stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 3. ACTUALIZAR (EDITAR) UNA TRANSACCIÓN
    public TransactionResponseDTO updateTransaction(Long id, TransactionRequestDTO requestDTO) {
        User user = currentUserService.get();

        // 1. Buscamos la transacción en la BD
        Transaction transaction = getOwnedTransaction(id, user); // Método de apoyo que busca la transacción y verifica la propiedad en un solo paso

        // 3. Buscamos la categoría (por si el usuario decidió cambiarla)
        Category category = getCategory(requestDTO.getCategoryId());

        // 5. Actualizamos los datos
        transactionMapper.updateEntity(transaction, requestDTO);
        transaction.setCategory(category);

        // 6. Guardamos y devolvemos
        return transactionMapper.toResponseDTO(transactionRepository.save(transaction));
    }

    // 4. BORRAR UNA TRANSACCIÓN
    @Transactional
    public void deleteTransaction(Long id, String email) {
        // 1. Buscamos la transacción
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));

        // 2. Verificamos que pertenezca al usuario (Seguridad)
        if (!transaction.getUser().getEmail().equals(email)) {
            throw new RuntimeException("No tienes permiso para borrar esta transacción");
        }

        // 3. Si la transacción está vinculada a una meta, restamos el dinero de la meta
        if (transaction.getGoal() != null) {
            Goal goal = transaction.getGoal();
            BigDecimal newAmount = goal.getSavedAmount().subtract(transaction.getAmount());

            // Evitamos que el ahorro sea negativo por si acaso
            if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
                newAmount = BigDecimal.ZERO;
            }

            goal.setSavedAmount(newAmount);
            goalRepository.save(goal); // Actualizamos la meta
        }

        // 4. Finalmente, borramos la transacción
        transactionRepository.delete(transaction);
    }

    // OBTENER EL RESUMEN FINANCIERO
    public TransactionSummaryDTO getSummary() {
        User user = currentUserService.get();
        BigDecimal income = Optional.ofNullable(transactionRepository.sumAmountByUserIdAndCategoryType(user.getId(), CategoryType.INCOME)).orElse(BigDecimal.ZERO);
        BigDecimal expense = Optional.ofNullable(transactionRepository.sumAmountByUserIdAndCategoryType(user.getId(), CategoryType.EXPENSE)).orElse(BigDecimal.ZERO);
        return new TransactionSummaryDTO(income, expense, income.subtract(expense));
    }

    // Métodos de apoyo mínimos
    private Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    private Transaction getOwnedTransaction(Long id, User user) {
        Transaction t = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
        if (!t.getUser().getId().equals(user.getId())) throw new RuntimeException("Acceso denegado");
        return t;
    }
}
