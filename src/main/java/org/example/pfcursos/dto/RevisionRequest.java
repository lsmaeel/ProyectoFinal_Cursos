package org.example.pfcursos.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RevisionRequest {
    @NotNull(message = "El ID del curso es obligatorio")
    private Long id_curso;

    @NotNull(message = "El ID del alumno es obligatorio")
    private Long id_alumno;

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 10, message = "La puntuación máxima es 10")
    private double puntuacion;

    private String comentario;
}
