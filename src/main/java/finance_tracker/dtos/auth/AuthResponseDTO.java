package finance_tracker.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // Lombok crea un constructor con todos los argumentos
@NoArgsConstructor  // Lombok crea un constructor vacío
public class AuthResponseDTO {

    private String token;

    // Opcional: A veces en el frontend (Angular) es muy útil que el backend
    // te devuelva también el nombre o el email para pintarlo en la barra de navegación
    // sin tener que desencriptar el token en el cliente. Lo dejo comentado por si lo quieres usar:
    // private String name;
    // private String email;
}
