package finance_tracker.dtos.user;

import lombok.Data;

@Data
//Imagina que en tu app de Angular pones una página que diga "Mi Perfil", donde el usuario puede ver sus datos. Necesitarás un endpoint (ej. GET /api/users/me)
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    // ¡No hay campo password!
}