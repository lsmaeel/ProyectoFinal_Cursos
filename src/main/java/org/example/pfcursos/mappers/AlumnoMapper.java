package org.example.pfcursos.mappers;

import org.example.pfcursos.dto.AlumnoResponse;
import org.example.pfcursos.modelo.Alumno;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AlumnoMapper {
    Alumno toEntity(AlumnoResponse alumnoResponse);

    @AfterMapping
    default void linkInscripciones(@MappingTarget Alumno alumno) {
        alumno.getInscripciones().forEach(inscripcione -> inscripcione.setAlumno(alumno));
    }

    @AfterMapping
    default void linkRevisiones(@MappingTarget Alumno alumno) {
        alumno.getRevisiones().forEach(revisione -> revisione.setAlumno(alumno));
    }

    AlumnoResponse toResponse(Alumno alumno);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Alumno partialUpdate(AlumnoResponse alumnoResponse, @MappingTarget Alumno alumno);
}