package org.example.pfcursos.mappers;

import org.example.pfcursos.dto.RevisionResponse;
import org.example.pfcursos.modelo.Revision;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RevisionMapper {
    Revision toEntity(RevisionResponse revisionResponse);

    RevisionResponse toResponse(Revision revision);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Revision partialUpdate(RevisionResponse revisionResponse, @MappingTarget Revision revision);
}