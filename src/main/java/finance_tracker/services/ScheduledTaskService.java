package finance_tracker.services;

import finance_tracker.models.Transaction;
import finance_tracker.repositories.TransactionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduledTaskService {

    private final TransactionRepository transactionRepository;

    public ScheduledTaskService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional // Para asegurarnos de que todo el proceso sea atómico
    public void processRecurringTransactions() {

        System.out.println("🤖 Ejecutando revisión de suscripciones automáticas...");

        // 1. ¿Qué día es hoy? (Ej: 30)
        LocalDate today = LocalDate.now();
        int currentDay = today.getDayOfMonth();

        // 2. Buscamos todas las plantillas que se crearon un día como hoy
        List<Transaction> templates = transactionRepository.findRecurringTransactionsByDay(currentDay);

        // 3. Por cada plantilla, creamos el cobro de este mes
        for (Transaction template : templates) {

            // Evitamos clonarla el mismo día que el usuario la creó originalmente
            if (template.getTransactionDate().isEqual(today)) {
                continue;
            }

            // Definimos cómo se llamará el cobro
            String automaticDescription = template.getDescription() + " (Automático)";

            // Preguntamos a la BD si YA hicimos este cobro hoy
            boolean alreadyChargedToday = transactionRepository.existsByUserIdAndDescriptionAndTransactionDate(
                    template.getUser().getId(),
                    automaticDescription,
                    today
            );

            // Si ya lo cobramos, saltamos a la siguiente plantilla
            if (alreadyChargedToday) {
                System.out.println("⏳ El cobro de '" + template.getDescription() + "' ya se realizó hoy. Saltando...");
                continue;
            }

            System.out.println("💸 Generando cobro automático para: " + template.getDescription());

            // Fabricamos el clon
            Transaction newCharge = new Transaction();
            newCharge.setAmount(template.getAmount());
            newCharge.setDescription(template.getDescription() + " (Automático)");
            newCharge.setTransactionDate(today);
            newCharge.setRecurring(false); // El cobro generado no es recurrente, solo la plantilla lo es
            newCharge.setCategory(template.getCategory());
            newCharge.setUser(template.getUser()); // Se asigna al mismo dueño

            // Guardamos el nuevo cobro
            transactionRepository.save(newCharge);
        }

        System.out.println("✅ Revisión de suscripciones terminada.");
    }
}
