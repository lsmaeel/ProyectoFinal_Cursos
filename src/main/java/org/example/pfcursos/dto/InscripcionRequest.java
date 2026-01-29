package org.example.pfcursos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor

public class InscripcionRequest {

    @NotNull(message = "El ID del alumno es obligatorio")
    private Long id_alumno;

    @NotNull(message = "El ID del curso es obligatorio")
    private Long id_curso;
}
