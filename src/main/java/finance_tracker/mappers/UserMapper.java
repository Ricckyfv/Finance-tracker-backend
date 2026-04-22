package finance_tracker.mappers;

import finance_tracker.dtos.auth.RegisterRequestDTO;
import finance_tracker.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Convierte el DTO de registro a la Entidad User
    User toEntity(RegisterRequestDTO requestDTO);
}
