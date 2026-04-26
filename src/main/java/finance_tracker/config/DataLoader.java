package finance_tracker.config;

import finance_tracker.models.Category;
import finance_tracker.models.CategoryType;
import finance_tracker.repositories.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataLoader(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Comprobando categorías en la Base de Datos...");

        // Categorías de GASTOS
        saveCategoryIfNotFound("Vivienda", CategoryType.EXPENSE);
        saveCategoryIfNotFound("Transporte", CategoryType.EXPENSE);
        saveCategoryIfNotFound("Servicios", CategoryType.EXPENSE);
        saveCategoryIfNotFound("Deudas", CategoryType.EXPENSE);
        saveCategoryIfNotFound("Supermercado", CategoryType.EXPENSE);
        saveCategoryIfNotFound("Ocio y restaurantes", CategoryType.EXPENSE);
        saveCategoryIfNotFound("Salud y Deporte", CategoryType.EXPENSE);
        saveCategoryIfNotFound("Viajes", CategoryType.EXPENSE);
        saveCategoryIfNotFound("Ropa y Compras", CategoryType.EXPENSE);
        saveCategoryIfNotFound("Otros Gastos", CategoryType.EXPENSE);
        saveCategoryIfNotFound("Ahorros", CategoryType.EXPENSE);

        // Categorías de INGRESOS
        saveCategoryIfNotFound("Salario", CategoryType.INCOME);
        saveCategoryIfNotFound("Otros Ingresos", CategoryType.INCOME);

        System.out.println("¡Categorías comprobadas/creadas con éxito!");
    }

    // Método auxiliar para evitar duplicados
    private void saveCategoryIfNotFound(String name, CategoryType type) {
        if (!categoryRepository.existsByName(name)) {
            categoryRepository.save(new Category(name, type));
            System.out.println("✅ Categoría creada: " + name);
        }
    }
}
