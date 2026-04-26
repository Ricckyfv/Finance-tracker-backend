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
            Category comida = new Category("Comida", CategoryType.EXPENSE);
            Category ocio = new Category("Ocio", CategoryType.EXPENSE);
            Category salud = new Category("Salud", CategoryType.EXPENSE);
            Category otrosGastos = new Category("Otros Gastos", CategoryType.EXPENSE);

            // Categorías de INGRESOS
            Category salario = new Category("Salario", CategoryType.INCOME);
            Category ahorros = new Category("Ahorros", CategoryType.INCOME); // Para el retorno de metas
            Category otrosIngresos = new Category("Otros Ingresos", CategoryType.INCOME);

            // Guardamos todas de golpe
            categoryRepository.saveAll(Arrays.asList(
                    vivienda, transporte, servicios, deudas, comida, ocio, salud, otrosGastos,
                    salario, ahorros, otrosIngresos
            ));

            System.out.println("¡Categorías creadas con éxito!");
        }
    }
}
