package finance_tracker.controllers;


import finance_tracker.dtos.auth.AuthResponseDTO;
import finance_tracker.dtos.auth.LoginRequestDTO;
import finance_tracker.dtos.auth.RegisterRequestDTO;
import finance_tracker.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Endpoint: POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        // Llamamos al servicio y devolvemos un 200 OK con el Token
        return ResponseEntity.ok(authService.register(request));
    }

    // Endpoint: POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        // Llamamos al servicio y devolvemos un 200 OK con el Token
        return ResponseEntity.ok(authService.login(request));
    }
}
