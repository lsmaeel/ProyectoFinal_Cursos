package org.example.pfcursos.mappers;

import org.example.pfcursos.dto.ProfesorResponse;
import org.example.pfcursos.modelo.Profesor;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfesorMapper {
    Profesor toEntity(ProfesorResponse profesorResponse);

    ProfesorResponse toResponse(Profesor profesor);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Profesor partialUpdate(ProfesorResponse profesorResponse, @MappingTarget Profesor profesor);
}