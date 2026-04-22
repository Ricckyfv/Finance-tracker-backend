package finance_tracker.controllers;

import finance_tracker.dtos.transaction.TransactionRequestDTO;
import finance_tracker.dtos.transaction.TransactionResponseDTO;
import finance_tracker.dtos.transaction.TransactionSummaryDTO;
import finance_tracker.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // POST /api/transactions
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Valid @RequestBody TransactionRequestDTO requestDTO) {
        return ResponseEntity.ok(transactionService.createTransaction(requestDTO));
    }

    // GET /api/transactions
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getMyTransactions() {
        return ResponseEntity.ok(transactionService.getMyTransactions());
    }

    // GET /api/transactions/summary
    @GetMapping("/summary")
    public ResponseEntity<TransactionSummaryDTO> getSummary() {
        return ResponseEntity.ok(transactionService.getSummary());
    }

    // PUT /api/transactions/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequestDTO requestDTO) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, requestDTO));
    }

    // DELETE /api/transactions/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        // 1. Extraemos el email del usuario del token de seguridad actual
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Ejecutamos el servicio (que ahora restará el dinero de la meta si corresponde)
        transactionService.deleteTransaction(id, email);

        // 3. Devolvemos un 204 No Content (Es el estándar REST para un borrado exitoso)
        return ResponseEntity.noContent().build();

    }
}