package finance_tracker.mappers;

import finance_tracker.dtos.goal.GoalRequest;
import finance_tracker.dtos.goal.GoalResponse;
import finance_tracker.models.Goal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GoalMapper {

    // 1. De Entidad a DTO
    GoalResponse toResponseDTO(Goal goal);

    // 2. De DTO a Entidad (Para crear uno nuevo)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "savedAmount", ignore = true) // El ahorro inicial siempre será el que diga la BD (0)
    Goal toEntity(GoalRequest dto);

    // 3. De DTO a Entidad existente (Para cuando hagas el Update en el futuro)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "savedAmount", ignore = true) // No dejamos que el usuario edite su dinero ahorrado desde el form general
    void updateEntity(@MappingTarget Goal entity, GoalRequest dto);

}


