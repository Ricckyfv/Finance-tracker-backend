package finance_tracker.services;


import finance_tracker.dtos.auth.AuthResponseDTO;
import finance_tracker.dtos.auth.LoginRequestDTO;
import finance_tracker.dtos.auth.RegisterRequestDTO;
import finance_tracker.mappers.UserMapper;
import finance_tracker.models.Role;
import finance_tracker.models.User;
import finance_tracker.repositories.UserRepository;
import finance_tracker.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    // Inyectamos todas nuestras herramientas de seguridad
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    // Método para REGISTRAR
    public AuthResponseDTO register(RegisterRequestDTO request) {

        // 1. Verificamos si el email ya está registrado
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está en uso");
        }

        // 2. Convertimos el DTO a Entidad
        User user = userMapper.toEntity(request);

        // 3. ¡ENCRIPTAMOS LA CONTRASEÑA ANTES DE GUARDAR!
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // LE ASIGNAMOS EL ROL BÁSICO POR DEFECTO
        user.setRole(Role.USER);

        // 4. Guardamos en la base de datos
        userRepository.save(user);

        // 5. Generamos el token JWT para que el usuario ya entre logueado
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponseDTO(token);
    }

    // Método para INICIAR SESIÓN
    public AuthResponseDTO login(LoginRequestDTO request) {

        // 1. El AuthenticationManager verifica si el email y la contraseña coinciden en la BD
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Si las credenciales son correctas, generamos el token
        String token = jwtUtil.generateToken(request.getEmail());

        return new AuthResponseDTO(token);
    }
}
