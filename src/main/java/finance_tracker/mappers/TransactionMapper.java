package finance_tracker.mappers;

import finance_tracker.dtos.transaction.TransactionResponseDTO;
import finance_tracker.dtos.transaction.TransactionRequestDTO;
import finance_tracker.models.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// componentModel = "spring" hace que puedas inyectarlo con un constructor (o @Autowired)
@Mapper(componentModel = "spring")
public interface TransactionMapper {

    // 1. De Entidad a DTO (Lectura)
    // Le decimos a MapStruct que entre al objeto "category" y saque sus datos
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "category.type", target = "categoryType")
    TransactionResponseDTO toResponseDTO(Transaction transaction);

    // 2. De DTO a Entidad existente (Para el Update)
    // Ignoramos id, user y category porque esos los gestionamos por seguridad en el Servicio
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntity(@MappingTarget Transaction entity, TransactionRequestDTO dto);
}
