package finance_tracker.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
//Si en esa misma página de "Mi Perfil" en Angular quieres poner un botón de "Editar mis datos", para recibir esa petición (PUT /api/users/me).
public class UserUpdateDTO {

    @NotBlank(message = "El nombre es obligatorio y no puede estar en blanco")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    @NotBlank(message = "El email es obligatorio y no puede estar en blanco")
    @Email(message = "Formato de email inválido")
    private String email;
}
