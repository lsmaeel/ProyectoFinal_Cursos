package org.example.pfcursos.mappers;

import org.example.pfcursos.dto.CursoResponse;
import org.example.pfcursos.modelo.Curso;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CursoMapper {
    Curso toEntity(CursoResponse cursoResponse);

    @AfterMapping
    default void linkInscripciones(@MappingTarget Curso curso) {
        curso.getInscripciones().forEach(inscripciones -> inscripciones.setCurso(curso));
    }

    @AfterMapping
    default void linkRevisiones(@MappingTarget Curso curso) {
        curso.getRevisiones().forEach(revisiones -> revisiones.setCurso(curso));
    }

    CursoResponse toResponse(Curso curso);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Curso partialUpdate(CursoResponse cursoResponse, @MappingTarget Curso curso);
}