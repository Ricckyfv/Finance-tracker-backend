package finance_tracker.config;

import finance_tracker.models.*;
import finance_tracker.repositories.CategoryRepository;
import finance_tracker.repositories.TransactionRepository;
import finance_tracker.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(CategoryRepository categoryRepository,
                          UserRepository userRepository,
                          TransactionRepository transactionRepository,
                          PasswordEncoder passwordEncoder
    ) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Comprobando categorías en la Base de Datos...");

        cargarCategorias();
        cargarUsuarioYTransacciones();

        System.out.println("¡Categorías comprobadas/creadas con éxito!");
    }

    private void cargarUsuarioYTransacciones() {
        String email = "user@test.com";

        // Buscamos si el usuario ya existe
        User testUser = userRepository.findByEmail(email).orElse(null);

        // Si no existe, lo creamos desde cero junto con sus datos simulados
        if (testUser == null) {
            testUser = new User();
            testUser.setName("Test User");
            testUser.setEmail(email);
            testUser.setPassword(passwordEncoder.encode("password"));
            testUser.setRole(Role.USER);
            testUser = userRepository.save(testUser);
            System.out.println("✅ Usuario de prueba creado: " + email);

            // Una vez creado el usuario, le generamos su historial financiero dinámico
            System.out.println("Generando historial financiero...");
            crearHistorialFinanciero(testUser);
        }
        else if(testUser.getRole() == null) {
            testUser.setRole(Role.USER);
            userRepository.save(testUser);
            System.out.println("✅ Rol reparado y actualizado para el usuario de prueba");
        }
    }

    private void crearHistorialFinanciero(User user) {
        // Recuperamos las categorías necesarias de la base de datos
        Category salario = categoryRepository.findByName("Salario").orElse(null);
        Category otrosIngresos = categoryRepository.findByName("Otros Ingresos").orElse(null);
        Category supermercado = categoryRepository.findByName("Supermercado").orElse(null);
        Category vivienda = categoryRepository.findByName("Vivienda").orElse(null);
        Category ocio = categoryRepository.findByName("Ocio y restaurantes").orElse(null);
        Category transporte = categoryRepository.findByName("Transporte").orElse(null);

        LocalDate hoy = LocalDate.now();

        // --- TRANSACCIONES DEL MES ACTUAL ---
        if (salario != null) crearTransaccion("Nómina Mensual", 2200.0, hoy.minusDays(5), salario, user);
        if (vivienda != null) crearTransaccion("Alquiler Piso", -850.0, hoy.minusDays(4), vivienda, user);
        if (supermercado != null) {
            crearTransaccion("Compra Semanal Mercadona", -65.40, hoy.minusDays(3), supermercado, user);
            crearTransaccion("Compra semanal Carrefour", -42.10, hoy.minusDays(1), supermercado, user);
        }
        if (ocio != null) crearTransaccion("Cena Fin de Semana", -48.00, hoy.minusDays(2), ocio, user);

        // --- TRANSACCIONES DEL MES ANTERIOR ---
        LocalDate mesPasado = hoy.minusMonths(1);
        if (salario != null) crearTransaccion("Nómina Mensual", 2200.0, mesPasado.withDayOfMonth(1), salario, user);
        if (otrosIngresos != null) crearTransaccion("Venta Wallapop", 120.0, mesPasado.withDayOfMonth(12), otrosIngresos, user);
        if (vivienda != null) crearTransaccion("Alquiler Piso", -850.0, mesPasado.withDayOfMonth(4), vivienda, user);
        if (supermercado != null) crearTransaccion("Compra Mensual Grande", -180.0, mesPasado.withDayOfMonth(6), supermercado, user);
        if (ocio != null) {
            crearTransaccion("Entradas Concierto", -90.0, mesPasado.withDayOfMonth(15), ocio, user);
            crearTransaccion("Suscripción Netflix", -17.99, mesPasado.withDayOfMonth(20), ocio, user);
        }
        if (transporte != null) crearTransaccion("Gasolina", -60.0, mesPasado.withDayOfMonth(10), transporte, user);

        System.out.println("✅ ¡Historial financiero simulado inyectado con éxito!");
    }

    private void crearTransaccion(String descripcion, double monto, LocalDate fecha, Category categoria, User usuario) {
        Transaction t = new Transaction();
        t.setDescription(descripcion);
        // Convertimos el double a BigDecimal, que es lo que pide tu modelo
        t.setAmount(java.math.BigDecimal.valueOf(monto));
        t.setTransactionDate(fecha);
        t.setCategory(categoria);
        t.setUser(usuario);
        t.setRecurring(false);

        transactionRepository.save(t);
    }

    private void cargarCategorias() {
        // Gastos
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

        // Ingresos
        saveCategoryIfNotFound("Salario", CategoryType.INCOME);
        saveCategoryIfNotFound("Otros Ingresos", CategoryType.INCOME);
    }

    // Método auxiliar para evitar duplicados
    private void saveCategoryIfNotFound(String name, CategoryType type) {
        if (!categoryRepository.existsByName(name)) {
            categoryRepository.save(new Category(name, type));
            System.out.println("✅ Categoría creada: " + name);
        }
    }
}
