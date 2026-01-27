package org.example.pfcursos.mappers;

import org.example.pfcursos.dto.InscripcionResponse;
import org.example.pfcursos.modelo.Inscripcion;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InscripcionMapper {
    Inscripcion toEntity(InscripcionResponse inscripcionResponse);

    InscripcionResponse toResponse(Inscripcion inscripcion);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Inscripcion partialUpdate(InscripcionResponse inscripcionResponse, @MappingTarget Inscripcion inscripcion);
}