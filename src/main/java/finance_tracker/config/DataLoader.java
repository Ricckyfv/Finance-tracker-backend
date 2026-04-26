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
        // Solo insertamos datos si la tabla está completamente vacía
        if (categoryRepository.count() == 0) {

            System.out.println("Cargando categorías por defecto en la Base de Datos...");

            // Categorías de GASTOS (Basado en lo que tenías en tu Frontend)
            Category vivienda = new Category("Vivienda", CategoryType.EXPENSE);
            Category transporte = new Category("Transporte", CategoryType.EXPENSE);
            Category servicios = new Category("Servicios", CategoryType.EXPENSE);
            Category deudas = new Category("Deudas", CategoryType.EXPENSE);
            Category comida = new Category("Supermercado", CategoryType.EXPENSE);
            Category ocio = new Category("Ocio y restaurantes", CategoryType.EXPENSE);
            Category salud = new Category("Salud y Deporte", CategoryType.EXPENSE);
            Category viajes = new Category("Viajes", CategoryType.EXPENSE);
            Category ropaCompras = new Category("Ropa y Compras", CategoryType.EXPENSE);
            Category otrosGastos = new Category("Otros Gastos", CategoryType.EXPENSE);
            Category ahorros = new Category("Ahorros", CategoryType.EXPENSE); // Para el retorno de metas


            // Categorías de INGRESOS
            Category salario = new Category("Salario", CategoryType.INCOME);
            Category otrosIngresos = new Category("Otros Ingresos", CategoryType.INCOME);

            // Guardamos todas de golpe
            categoryRepository.saveAll(Arrays.asList(
                    vivienda, transporte, servicios, deudas, comida, ocio, salud, otrosGastos, viajes, ropaCompras,
                    ahorros, salario, otrosIngresos
            ));

            System.out.println("¡Categorías creadas con éxito!");
        }
    }

    // Método auxiliar para evitar duplicados
    private void saveCategoryIfNotFound(String name, CategoryType type) {
        if (!categoryRepository.existsByName(name)) {
            categoryRepository.save(new Category(name, type));
            System.out.println("Categoría creada: " + name);
        }
    }
}
