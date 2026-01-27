package org.example.pfcursos.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.pfcursos.modelo.Inscripcion}
 */
@Value
public class InscripcionResponse implements Serializable {
    Long id_revision;
    LocalDateTime fechaInscripcion;
    CursoDto curso;
    AlumnoDto alumno;

    /**
     * DTO for {@link org.example.pfcursos.modelo.Curso}
     */
    @Value
    public static class CursoDto implements Serializable {
        Long id_curso;
        String titulo;
    }

    /**
     * DTO for {@link org.example.pfcursos.modelo.Alumno}
     */
    @Value
    public static class AlumnoDto implements Serializable {
        Long id_alumno;
        String nombre;
    }
}