package finance_tracker.services;

import finance_tracker.models.Category;
import finance_tracker.models.User;
import finance_tracker.repositories.BudgetRepository;
import finance_tracker.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;

    public BudgetService(BudgetRepository budgetRepository, TransactionRepository transactionRepository) {
        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
    }

    public boolean isBudgetExceeded(User user, Category category, BigDecimal newAmount) {
        LocalDate now = LocalDate.now();
        return budgetRepository.findByUserIdAndCategoryIdAndMonthAndYear(
                        user.getId(), category.getId(), now.getMonthValue(), now.getYear())
                .map(budget -> {
                    BigDecimal currentSpent = transactionRepository.sumAmountByUserIdAndCategoryIdAndMonthAndYear(
                            user.getId(), category.getId(), now.getMonthValue(), now.getYear());
                    if (currentSpent == null) currentSpent = BigDecimal.ZERO;
                    return currentSpent.add(newAmount).compareTo(budget.getAmountLimit()) > 0;
                }).orElse(false);
    }
}
